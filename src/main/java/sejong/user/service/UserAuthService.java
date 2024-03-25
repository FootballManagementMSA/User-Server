package sejong.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import sejong.user.aws.S3Service;
import sejong.user.entity.User;
import sejong.user.global.exception.AuthorizationException;
import sejong.user.global.exception.BadRequestException;
import sejong.user.global.exception.NotFoundException;
import sejong.user.service.dto.UserAuthDto;
import sejong.user.repository.UserRepository;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static sejong.user.global.exception.constant.ExceptionMessageConstant.*;
import static sejong.user.global.res.constant.StatusCodeConstant.*;
import static sejong.user.service.constant.UserAuthServiceConstant.*;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final S3Service s3Service;

    @Transactional
    public UserAuthDto.UserLoginResponse userLogin(UserAuthDto.UserAuthRequest userAuthRequest) {
        User user = validateLoginUser(userAuthRequest.getId(), userAuthRequest.getPw());

        String accessToken = tokenService.createAccessToken(userAuthRequest.getId());
        String refreshToken = tokenService.createRefreshToken(userAuthRequest.getId());

        saveTokensInRedis(userAuthRequest.getId(), accessToken, refreshToken);

        return UserAuthDto.UserLoginResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void registerUser(UserAuthDto.UserRegisterRequest userRegisterRequest) throws IOException {
        UserAuthDto.UserAuthRequest userAuthRequest
                = convertToUserAuthRequest(userRegisterRequest.getStudentId(), userRegisterRequest.getPassword());

        ResponseEntity<String> response = getUserAuthData(userAuthRequest);
        String responseBody = response.getBody();
        JSONObject jsonObject = new JSONObject(responseBody);

        // 예외처리
        String resultCode = jsonObject.getJSONObject(RESULT).getString(CODE);
        validateAuthSejongStudent(resultCode);
        validateAlreadyRegisteredUser(userRegisterRequest.getStudentId());

        String image = "";
        if(!userRegisterRequest.getImage().isEmpty()) image = s3Service.uploadMultipartFile(userRegisterRequest.getImage());

        UserAuthDto.UserDto userDto
                = extractStudentInfo(jsonObject, userRegisterRequest, image);

        userRepository.save(convertToUser(userDto));
    }

    @Transactional
    public UserAuthDto.ReissueTokenResponse reissueToken(UserAuthDto.ReissueTokenRequest reissueTokenRequest) {
        String studentId = tokenService.getStudentIdFromToken(reissueTokenRequest.getRefreshToken());
        validateRefreshToken(studentId, reissueTokenRequest.getRefreshToken());

        deleteExistingTokens(studentId);

        String newAccessToken = tokenService.createAccessToken(studentId);
        String newRefreshToken = tokenService.createRefreshToken(studentId);
        saveTokensInRedis(studentId, newAccessToken, newRefreshToken);

        return convertReissueTokenResponse(newAccessToken, newRefreshToken);
    }

    // --->
    private ResponseEntity<String> getUserAuthData(UserAuthDto.UserAuthRequest userAuthRequest) {
        String url = SEJONG_AUTH_URL;

        HttpHeaders headers = makeRequestHeader();
        JSONObject requestBody = makeJSONObject(userAuthRequest);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        return restTemplate.postForEntity(url, entity, String.class);
    }

    private HttpHeaders makeRequestHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    private JSONObject makeJSONObject(UserAuthDto.UserAuthRequest userAuthRequest) {
        JSONObject requestBody = new JSONObject();
        requestBody.put(ID, userAuthRequest.getId());
        requestBody.put(PASSWORD, userAuthRequest.getPw());

        return requestBody;
    }

    private UserAuthDto.UserDto extractStudentInfo(JSONObject jsonObject, UserAuthDto.UserRegisterRequest userRegisterRequest, String image) {
        JSONObject body = jsonObject.getJSONObject(RESULT).getJSONObject(BODY);

        return UserAuthDto.UserDto.builder()
                .studentId(userRegisterRequest.getStudentId())
                .password(userRegisterRequest.getPassword())
                .age(userRegisterRequest.getAge())
                .height(userRegisterRequest.getHeight())
                .foot(userRegisterRequest.getFoot())
                .position(userRegisterRequest.getPosition())
                .sex(userRegisterRequest.getSex())
                .image(image)
                .grade(body.getString(GRADE))
                .major(body.getString(MAJOR))
                .name(body.getString(NAME))
                .status(body.getString(STATUS))
                .build();
    }
    // <-- Request Header 와 관련된 메서드

    // -->
    private void validateAuthSejongStudent(String resultCode) {
        if (resultCode.equals(AUTH_FAIL))
            throw new NotFoundException(NOT_FOUND_STATUS_CODE, SEJONG_AUTH_FAIL_EXCEPTION_MESSAGE);
    }

    private User validateLoginUser(String studentId, String password) {
        User findUser = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));

        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new BadRequestException(BAD_REQUEST_STATUS_CODE, ID_PASSWORD_MISMATCH_EXCEPTION_MESSAGE);
        }

        return findUser;
    }

    private void validateAlreadyRegisteredUser(String studentId) {
        Optional<User> findUser = userRepository.findByStudentId(studentId);
        if (findUser.isPresent())
            throw new BadRequestException(BAD_REQUEST_STATUS_CODE, ALREADY_REGISTERED_USER);
    }

    private void validateRefreshToken(String studentId, String refreshToken) {
        String refreshTokenKey = REFRESH_TOKEN + studentId;
        String storedRefreshToken = (String) redisTemplate.opsForValue().get(refreshTokenKey);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new AuthorizationException(AUTHORIZATION_STATUS_CODE, AUTHORIZATION_EXCEPTION_MESSAGE);
        }
    }
    // <-- 예외처리 메서드

    // -->
    private UserAuthDto.UserAuthRequest convertToUserAuthRequest(String studentId, String password) {
        return UserAuthDto.UserAuthRequest.builder()
                .id(studentId)
                .pw(password)
                .build();
    }

    private User convertToUser(UserAuthDto.UserDto userDto) {
        return User.builder()
                .studentId(userDto.getStudentId())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .grade(userDto.getGrade())
                .age(userDto.getAge())
                .foot(userDto.getFoot())
                .height(userDto.getHeight())
                .sex(userDto.getSex())
                .name(userDto.getName())
                .major(userDto.getMajor())
                .position(userDto.getPosition())
                .status(userDto.getStatus())
                .game(0)
                .goal(0)
                .image(userDto.getImage())
                .build();
    }

    private UserAuthDto.ReissueTokenResponse convertReissueTokenResponse(String accessToken, String refreshToken) {
        return UserAuthDto.ReissueTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    // <-- 클래스 변환 메서드

    // -->
    public void saveTokensInRedis(String studentId, String accessToken, String refreshToken) {
        // AccessToken 저장
        String accessTokenKey = ACCESS_TOKEN + studentId;
        redisTemplate.opsForValue().set(accessTokenKey, accessToken, Duration.ofHours(ACCESS_TOKEN_DURATION_TIME));

        // RefreshToken 저장
        String refreshTokenKey = REFRESH_TOKEN + studentId;
        redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, Duration.ofDays(REFRESH_TOKEN_DURATION_TIME));
    }

    private void deleteExistingTokens(String studentId) {
        String accessTokenKey = ACCESS_TOKEN + studentId;
        String refreshTokenKey = REFRESH_TOKEN + studentId;
        redisTemplate.delete(accessTokenKey);
        redisTemplate.delete(refreshTokenKey);
    }
    // <-- Redis 관련 메서드

}