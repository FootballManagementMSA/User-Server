package sejong.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import sejong.user.entity.User;
import sejong.user.global.exception.BadRequestException;
import sejong.user.global.exception.NotFoundException;
import sejong.user.service.dto.UserAuthDto;
import sejong.user.repository.UserRepository;

import java.time.Duration;
import java.util.Optional;

import static sejong.user.global.exception.constant.ExceptionMessageConstant.*;
import static sejong.user.global.res.constant.StatusCodeConstant.BAD_REQUEST_STATUS_CODE;
import static sejong.user.global.res.constant.StatusCodeConstant.NOT_FOUND_STATUS_CODE;
import static sejong.user.service.constant.UserAuthServiceConstant.*;

@Service
@Transactional
public class UserAuthService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserAuthService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TokenService tokenService,
                           RedisTemplate<String, Object> redisTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.redisTemplate = redisTemplate;
    }

    public UserAuthDto.UserLoginResponse userLogin(UserAuthDto.UserAuthRequest userAuthRequest) {
        validateRegisterUser(userAuthRequest.getId());
        validateLoginUser(userAuthRequest.getId(), userAuthRequest.getPw());

        String accessToken = tokenService.createAccessToken(userAuthRequest.getId());
        String refreshToken = tokenService.createRefreshToken(userAuthRequest.getId());

        saveTokensInRedis(userAuthRequest.getId(), accessToken, refreshToken);

        return UserAuthDto.UserLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void registerUser(UserAuthDto.UserRegisterRequest userRegisterRequest) {
        UserAuthDto.UserAuthRequest userAuthRequest
                = convertToUserAuthRequest(userRegisterRequest.getStudentId(), userRegisterRequest.getPassword());

        ResponseEntity<String> response = getUserAuthData(userAuthRequest);
        String responseBody = response.getBody();
        JSONObject jsonObject = new JSONObject(responseBody);

        // 예외처리
        String resultCode = jsonObject.getJSONObject(RESULT).getString(CODE);
        validateAuthSejongStudent(resultCode);
        validateAlreadyRegisteredUser(userRegisterRequest.getStudentId());

        UserAuthDto.UserDto userDto
                = extractStudentInfo(jsonObject, userRegisterRequest);

        userRepository.save(convertToUser(userDto));
    }

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

    private void validateAuthSejongStudent(String resultCode) {
        if (resultCode.equals(AUTH_FAIL))
            throw new NotFoundException(NOT_FOUND_STATUS_CODE, SEJONG_AUTH_FAIL_EXCEPTION_MESSAGE);
    }

    private void validateRegisterUser(String studentId) {
        userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));
    }

    private void validateAlreadyRegisteredUser(String studentId) {
        Optional<User> findUser = userRepository.findByStudentId(studentId);
        if (findUser.isPresent())
            throw new BadRequestException(BAD_REQUEST_STATUS_CODE, ALREADY_REGISTERED_USER);
    }

    private void validateLoginUser(String studentId, String password){
        Optional<User> findUser = userRepository.findByStudentId(studentId);

        if(!passwordEncoder.matches(password, findUser.get().getPassword())){
            throw new BadRequestException(BAD_REQUEST_STATUS_CODE, ID_PASSWORD_MISMATCH_EXCEPTION_MESSAGE);
        }
    }

    private UserAuthDto.UserDto extractStudentInfo(JSONObject jsonObject, UserAuthDto.UserRegisterRequest userRegisterRequest) {
        JSONObject body = jsonObject.getJSONObject(RESULT).getJSONObject(BODY);

        return UserAuthDto.UserDto.builder()
                .studentId(userRegisterRequest.getStudentId())
                .password(userRegisterRequest.getPassword())
                .age(userRegisterRequest.getAge())
                .height(userRegisterRequest.getHeight())
                .foot(userRegisterRequest.getFoot())
                .position(userRegisterRequest.getPosition())
                .sex(userRegisterRequest.getSex())
                .grade(body.getString(GRADE))
                .major(body.getString(MAJOR))
                .name(body.getString(NAME))
                .status(body.getString(STATUS))
                .build();
    }

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
                .build();
    }

    public void saveTokensInRedis(String userId, String accessToken, String refreshToken) {
        // AccessToken 저장
        String accessTokenKey = "accessToken:" + userId;
        redisTemplate.opsForValue().set(accessTokenKey, accessToken, Duration.ofHours(2)); // 1시간 동안 유효

        // RefreshToken 저장
        String refreshTokenKey = "refreshToken:" + userId;
        redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, Duration.ofDays(3)); // 7일 동안 유효
    }

}