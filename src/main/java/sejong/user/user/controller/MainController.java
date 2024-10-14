package sejong.user.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.user.feignClient.dto.ScheduleInfoDto;
import sejong.user.global.response.DataResponse;
import sejong.user.user.service.MainService;
import sejong.user.token.service.TokenService;
import sejong.user.user.dto.MainDto;

import java.util.List;

import static sejong.user.global.response.constant.ResponseMessageConstant.SUCCESS;
import static sejong.user.global.response.constant.StatusCodeConstant.OK_STATUS_CODE;

@RestController
@RequestMapping("/api/user-service")
@RequiredArgsConstructor
@Tag(name = "Main Home API", description = "메인 홈과 관련된 API 입니다.")
public class MainController {
    private final TokenService tokenService;
    private final MainService mainService;


    @GetMapping("/student")
    @Operation(summary = "Main Home", description = "Main Home 화면의 Student 정보")
    public ResponseEntity<DataResponse> mainStudentInfo(HttpServletRequest http) {
        String token = tokenService.getTokenFromRequest(http);
        String studentId = tokenService.getStudentIdFromToken(token);

        MainDto.StudentInfoResponse studentInfoResponse = mainService.studentInfo(studentId);

        return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, studentInfoResponse));
    }

    @GetMapping("/schedule")
    @Operation(summary = "Main Home", description = "Main Home 화면의 Schedule 정보")
    public ResponseEntity<DataResponse> mainScheduleInfo(HttpServletRequest http) {
        String token = tokenService.getTokenFromRequest(http);
        String studentId = tokenService.getStudentIdFromToken(token);

        List<ScheduleInfoDto> scheduleInfo = mainService.scheduleInfo(studentId);

        return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, scheduleInfo));
    }

    @GetMapping("/team")
    @Operation(summary = "Team Info", description = "TEAM-SERVER의 TEAM 정보를 가져오는 API 입니다.")
    public ResponseEntity<DataResponse> getRegisteredTeamList(HttpServletRequest http) {
        String token = tokenService.getTokenFromRequest(http);
        String studentId = tokenService.getStudentIdFromToken(token);

        List<MainDto.RegisteredTeamInfoResponse> registeredTeamInfo = mainService.getRegisteredTeamInfo(studentId);

        return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, registeredTeamInfo));
    }

}
