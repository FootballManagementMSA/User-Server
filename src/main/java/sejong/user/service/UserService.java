package sejong.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import sejong.user.entity.User;
import sejong.user.global.exception.NotFoundException;
import sejong.user.repository.UserRepository;
import sejong.user.service.dto.UserDto;

import static sejong.user.global.exception.constant.ExceptionMessageConstant.NOT_REGISTER_USER_EXCEPTION_MESSAGE;
import static sejong.user.global.exception.constant.StatusCodeConstant.NOT_FOUND_STATUS_CODE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserDto.MyPageResponse getMyPage(String studentId) {
        User requestUser = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));

        return changeUserToMyPageResponse(requestUser);
    }

    private UserDto.MyPageResponse changeUserToMyPageResponse(User user) {
        return UserDto.MyPageResponse.builder()
                .studentId(user.getStudentId())
                .name(user.getName())
                .image(user.getImage())
                .build();
    }
}
