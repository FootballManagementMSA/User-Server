package sejong.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.user.aws.S3Service;
import sejong.user.entity.User;
import sejong.user.global.exception.NotFoundException;
import sejong.user.repository.UserRepository;
import sejong.user.service.dto.UserDto;

import java.io.IOException;

import static sejong.user.global.exception.constant.ExceptionMessageConstant.NOT_REGISTER_USER_EXCEPTION_MESSAGE;
import static sejong.user.global.res.constant.StatusCodeConstant.NOT_FOUND_STATUS_CODE;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public UserDto.MyPageResponse getMyPage(String studentId) {
        User requestUser = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));

        return changeUserToMyPageResponse(requestUser);
    }

    @Transactional
    public void modifyUser(String studentId, UserDto.ModifyUserRequest modifyUserDto) throws IOException {
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));

        String imageURL = null;
        if (!modifyUserDto.getImage().equals(null)) {
            imageURL = s3Service.uploadMultipartFile(modifyUserDto.getImage());
        }

        user.updateUser(modifyUserDto, imageURL);
    }

    private UserDto.MyPageResponse changeUserToMyPageResponse(User user) {
        return UserDto.MyPageResponse.builder()
                .studentId(user.getStudentId())
                .name(user.getName())
                .image(user.getImage())
                .build();
    }
}
