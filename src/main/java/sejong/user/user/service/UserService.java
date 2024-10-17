package sejong.user.user.service;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sejong.user.global.utils.S3Utils;
import sejong.user.user.domain.UserDomain;
import sejong.user.user.entity.UserEntity;
import sejong.user.global.exception.NotFoundException;
import sejong.user.user.repository.UserRepository;
import sejong.user.user.kafka.producer.UserKafkaProducer;

import java.io.IOException;

import static sejong.user.global.exception.constant.ExceptionMessageConstant.NOT_REGISTER_USER_EXCEPTION_MESSAGE;
import static sejong.user.global.response.constant.StatusCodeConstant.NOT_FOUND_STATUS_CODE;
import static sejong.user.user.dto.UserDto.*;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final S3Utils s3Utils;
	private final UserKafkaProducer userKafkaProducer;

	private final UserDomain userDomain;

	@Transactional(readOnly = true)
	public GetUserResponse getUser(String studentId) {
		UserEntity user = userDomain.validateUser(studentId);

		return userDomain.changeUserToMyPageResponse(user);
	}

	@Transactional
	public void modifyUser(ModifyUserRequest modifyUserDto) throws IOException {
		UserEntity user = userDomain.validateUser(modifyUserDto.getStudentId());

		user.updateUser(modifyUserDto);
	}

	@Transactional
	public void logout(String studentId) {
		userDomain.validateUser(studentId);
		userDomain.deleteExistingTokens(studentId);
	}

	@Transactional
	public void deleteUser(String studentId) {
		UserEntity user = userRepository.findByStudentId(studentId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));

		userKafkaProducer.deleteUser(studentId);
		userRepository.delete(user);
	}

	@Transactional
	@KafkaListener(topics = "user-delete-rollback", groupId = "group-01")
	public void handleDeleteUserRollback(String message) {
		System.out.println("Received rollback message: " + message);
	}
}
