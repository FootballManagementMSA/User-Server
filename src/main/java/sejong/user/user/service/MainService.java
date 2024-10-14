package sejong.user.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.user.team.feignClient.TeamServiceClient;
import sejong.user.team.feignClient.dto.ScheduleInfoDto;
import sejong.user.user.entity.User;
import sejong.user.team.entity.UserTeam;
import sejong.user.global.exception.NotFoundException;
import sejong.user.user.repository.UserRepository;
import sejong.user.user.repository.UserTeamRepository;
import sejong.user.user.dto.MainDto;

import java.util.ArrayList;
import java.util.List;

import static sejong.user.global.exception.constant.ExceptionMessageConstant.NOT_REGISTER_USER_EXCEPTION_MESSAGE;
import static sejong.user.global.response.constant.StatusCodeConstant.NOT_FOUND_STATUS_CODE;

@Service
@RequiredArgsConstructor
public class MainService {
    private final UserRepository userRepository;
    private final TeamServiceClient teamServiceClient;
    private final UserTeamRepository userTeamRepository;

    // -->
    public MainDto.StudentInfoResponse studentInfo(String studentId) {
        User user = validateUser(studentId);

        return MainDto.StudentInfoResponse.builder()
                .name(user.getName())
                .game(user.getGame())
                .goal(user.getGoal())
                .position(user.getPosition())
                .foot(user.getFoot())
                .image(user.getImage())
                .age(user.getAge())
                .build();
    }

    public List<ScheduleInfoDto> scheduleInfo(String studentId) {
        User user = validateUser(studentId);
        return teamServiceClient.getScheduleInfo(user.getId()).getBody();
    }

    public List<MainDto.RegisteredTeamInfoResponse> getRegisteredTeamInfo(String studentId) {
        User user = validateUser(studentId);
        List<UserTeam> userTeams = userTeamRepository.findAllByUser(user);
        List<Long> teamIds = new ArrayList<>();
        for(UserTeam userTeam : userTeams) teamIds.add(userTeam.getTeamId());

        return teamServiceClient.getRegisteredTeamInfo(teamIds).getBody();
    }
    // <-- 호출 Method

    // -->
    private User validateUser(String studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));
    }
    // --> 예외처리 메서드
}
