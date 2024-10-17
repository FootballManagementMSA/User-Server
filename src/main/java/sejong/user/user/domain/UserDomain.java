package sejong.user.user.domain;

import static sejong.user.global.exception.constant.ExceptionMessageConstant.*;
import static sejong.user.global.response.constant.StatusCodeConstant.*;
import static sejong.user.user.constant.UserAuthServiceConstant.*;
import static sejong.user.user.dto.UserAuthDto.*;
import static sejong.user.user.dto.UserDto.*;

import java.time.Duration;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import sejong.user.global.exception.AuthorizationException;
import sejong.user.global.exception.BadRequestException;
import sejong.user.global.exception.NotFoundException;
import sejong.user.user.entity.UserEntity;
import sejong.user.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserDomain {
	private final RedisTemplate<String, Object> redisTemplate;
	private final RestTemplate restTemplate = new RestTemplate();
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserEntity validateUser(String studentId) {
		return userRepository.findByStudentId(studentId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));
	}

	public void deleteExistingTokens(String studentId) {
		String accessTokenKey = ACCESS_TOKEN + studentId;
		String refreshTokenKey = REFRESH_TOKEN + studentId;

		redisTemplate.delete(accessTokenKey);
		redisTemplate.delete(refreshTokenKey);
	}

	public GetUserResponse changeUserToMyPageResponse(UserEntity user) {
		return GetUserResponse.builder()
			.studentId(user.getStudentId())
			.name(user.getName())
			.build();
	}

	public ResponseEntity<String> getUserAuthData(UserAuthRequest userAuthRequest) {
		HttpHeaders headers = makeRequestHeader();
		JSONObject requestBody = makeJSONObject(userAuthRequest);

		HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

		return restTemplate.postForEntity(SEJONG_AUTH_URL, entity, String.class);
	}

	private HttpHeaders makeRequestHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

	private JSONObject makeJSONObject(UserAuthRequest userAuthRequest) {
		JSONObject requestBody = new JSONObject();
		requestBody.put(ID, userAuthRequest.getId());
		requestBody.put(PASSWORD, userAuthRequest.getPw());

		return requestBody;
	}

	public UserDto extractStudentInfo(JSONObject jsonObject, UserRegisterRequest userRegisterRequest) {
		JSONObject body = jsonObject.getJSONObject(RESULT).getJSONObject(BODY);

		return UserDto.builder()
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

	public void validateAuthSejongStudent(String resultCode) {
		if (resultCode.equals(AUTH_FAIL))
			throw new NotFoundException(NOT_FOUND_STATUS_CODE, SEJONG_AUTH_FAIL_EXCEPTION_MESSAGE);
	}

	public UserEntity validateLoginUser(String studentId, String password) {
		UserEntity findUser = userRepository.findByStudentId(studentId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));

		if (!passwordEncoder.matches(password, findUser.getPassword())) {
			throw new BadRequestException(BAD_REQUEST_STATUS_CODE, ID_PASSWORD_MISMATCH_EXCEPTION_MESSAGE);
		}

		return findUser;
	}

	public void validateAlreadyRegisteredUser(String studentId) {
		Optional<UserEntity> findUser = userRepository.findByStudentId(studentId);
		if (findUser.isPresent())
			throw new BadRequestException(BAD_REQUEST_STATUS_CODE, ALREADY_REGISTERED_USER);
	}

	public void validateRefreshToken(String studentId, String refreshToken) {
		String refreshTokenKey = REFRESH_TOKEN + studentId;
		String storedRefreshToken = (String)redisTemplate.opsForValue().get(refreshTokenKey);
		if (!refreshToken.equals(storedRefreshToken)) {
			throw new AuthorizationException(AUTHORIZATION_STATUS_CODE, AUTHORIZATION_EXCEPTION_MESSAGE);
		}
	}

	public UserAuthRequest convertToUserAuthRequest(String studentId, String password) {
		return UserAuthRequest.builder()
			.id(studentId)
			.pw(password)
			.build();
	}

	public UserEntity convertToUser(UserDto userDto) {
		return UserEntity.builder()
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
			.build();
	}

	public ReissueTokenResponse convertReissueTokenResponse(String accessToken, String refreshToken) {
		return ReissueTokenResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public void saveTokensInRedis(String studentId, String accessToken, String refreshToken) {
		// AccessToken 저장
		String accessTokenKey = ACCESS_TOKEN + studentId;
		redisTemplate.opsForValue().set(accessTokenKey, accessToken, Duration.ofHours(ACCESS_TOKEN_DURATION_TIME));

		// RefreshToken 저장
		String refreshTokenKey = REFRESH_TOKEN + studentId;
		redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, Duration.ofDays(REFRESH_TOKEN_DURATION_TIME));
	}
}
