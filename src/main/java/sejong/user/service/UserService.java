package sejong.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sejong.user.aws.S3Service;
import sejong.user.common.client.TeamServiceClient;
import sejong.user.entity.User;
import sejong.user.global.exception.NotFoundException;
import sejong.user.repository.UserRepository;
import sejong.user.service.dto.UserDto;

import java.io.IOException;

import static sejong.user.global.exception.constant.ExceptionMessageConstant.NOT_REGISTER_USER_EXCEPTION_MESSAGE;
import static sejong.user.global.res.constant.StatusCodeConstant.NOT_FOUND_STATUS_CODE;
import static sejong.user.service.constant.UserAuthServiceConstant.ACCESS_TOKEN;
import static sejong.user.service.constant.UserAuthServiceConstant.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final RedisTemplate<String, Object> redisTemplate;
    private final TeamServiceClient teamServiceClient;

    public UserDto.MyPageResponse getMyPage(String studentId) {
        User user = validateUser(studentId);

        return changeUserToMyPageResponse(user);
    }

    @Transactional
    public void modifyUser(String studentId, UserDto.ModifyUserRequest modifyUserDto) throws IOException {
        User user = validateUser(studentId);

        String imageURL = null;
        if (!modifyUserDto.getImage().equals(null)) {
            imageURL = s3Service.uploadMultipartFile(modifyUserDto.getImage());
        }

        user.updateUser(modifyUserDto, imageURL);
    }

    @Transactional
    public void logout(String studentId) {
        validateUser(studentId);
        deleteExistingTokens(studentId);
    }

    @Transactional
    public void deleteUser(String studentId) {
        User user = validateUser(studentId);
        userRepository.delete(user);
        teamServiceClient.deleteUserSquad(user.getId());
    }

    // -->
    private User validateUser(String studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));
    }
    // <-- 예외처리 관련 메서드

    // -->
    private void deleteExistingTokens(String studentId) {
        String accessTokenKey = ACCESS_TOKEN + studentId;
        String refreshTokenKey = REFRESH_TOKEN + studentId;
        redisTemplate.delete(accessTokenKey);
        redisTemplate.delete(refreshTokenKey);
    }
    // <-- Redis 관련 메서드

    // -->
    private UserDto.MyPageResponse changeUserToMyPageResponse(User user) {
        return UserDto.MyPageResponse.builder()
                .studentId(user.getStudentId())
                .name(user.getName())
                .image(user.getImage())
                .build();
    }
    // <-- 클래스 변환 메서드
}
