package sejong.user.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import sejong.user.global.utils.S3Utils;
import sejong.user.user.domain.UserDomain;
import sejong.user.user.entity.UserEntity;
import sejong.user.global.utils.TokenUtils;
import sejong.user.user.repository.UserRepository;

import java.io.IOException;

import static sejong.user.user.constant.UserAuthServiceConstant.*;
import static sejong.user.user.dto.UserAuthDto.*;

@Service
@RequiredArgsConstructor
public class UserAuthService {
	private final UserRepository userRepository;

	private final TokenUtils tokenUtils;
	private final S3Utils s3Utils;

	private final UserDomain userDomain;

	@Transactional
	public UserLoginResponse userLogin(UserAuthRequest userAuthRequest) {
		UserEntity user = userDomain.validateLoginUser(userAuthRequest.getId(), userAuthRequest.getPw());

		String accessToken = tokenUtils.createAccessToken(userAuthRequest.getId());
		String refreshToken = tokenUtils.createRefreshToken(userAuthRequest.getId());

		userDomain.saveTokensInRedis(userAuthRequest.getId(), accessToken, refreshToken);

		return UserLoginResponse.builder()
			.userId(user.getUserSeq())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	@Transactional
	public void registerUser(UserRegisterRequest userRegisterRequest) throws IOException {
		UserAuthRequest userAuthRequest = userDomain.convertToUserAuthRequest(
			userRegisterRequest.getStudentId(),
			userRegisterRequest.getPassword()
		);

		ResponseEntity<String> response = userDomain.getUserAuthData(userAuthRequest);
		String responseBody = response.getBody();
		JSONObject jsonObject = new JSONObject(responseBody);

		// 예외처리
		String resultCode = jsonObject.getJSONObject(RESULT).getString(CODE);
		userDomain.validateAuthSejongStudent(resultCode);
		userDomain.validateAlreadyRegisteredUser(userRegisterRequest.getStudentId());

		UserDto userDto = userDomain.extractStudentInfo(jsonObject, userRegisterRequest);

		userRepository.save(userDomain.convertToUser(userDto));
	}

	@Transactional
	public ReissueTokenResponse reissueToken(ReissueTokenRequest reissueTokenRequest) {
		String studentId = tokenUtils.getStudentIdFromToken(reissueTokenRequest.getRefreshToken());
		userDomain.validateRefreshToken(studentId, reissueTokenRequest.getRefreshToken());

		userDomain.deleteExistingTokens(studentId);

		String newAccessToken = tokenUtils.createAccessToken(studentId);
		String newRefreshToken = tokenUtils.createRefreshToken(studentId);
		userDomain.saveTokensInRedis(studentId, newAccessToken, newRefreshToken);

		return userDomain.convertReissueTokenResponse(newAccessToken, newRefreshToken);
	}
}