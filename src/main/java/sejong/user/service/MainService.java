package sejong.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.user.entity.User;
import sejong.user.global.exception.NotFoundException;
import sejong.user.repository.UserRepository;
import sejong.user.service.dto.MainDto;

import static sejong.user.global.exception.constant.ExceptionMessageConstant.NOT_REGISTER_USER_EXCEPTION_MESSAGE;
import static sejong.user.global.res.constant.StatusCodeConstant.NOT_FOUND_STATUS_CODE;

@Service
@RequiredArgsConstructor
public class MainService {
    private final UserRepository userRepository;
    public MainDto.StudentInfoResponse studentInfo(String studentId) {
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));

        return MainDto.StudentInfoResponse.builder()
                .name(user.getName())
                .game(user.getGame())
                .goal(user.getGoal())
                .position(user.getPosition())
                .foot(user.getFoot())
                .build();
    }
}
