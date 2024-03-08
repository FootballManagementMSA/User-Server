package sejong.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.user.common.client.dto.ScheduleInfoDto;
import sejong.user.global.res.DataResponse;
import sejong.user.service.MainService;
import sejong.user.service.TokenService;
import sejong.user.service.dto.MainDto;

import java.util.List;

import static sejong.user.global.res.constant.ResponseMessageConstant.SUCCESS;
import static sejong.user.global.res.constant.StatusCodeConstant.OK_STATUS_CODE;

@RestController
@RequestMapping("/api/user-service")
@RequiredArgsConstructor
public class MainController {
    private final TokenService tokenService;
    private final MainService mainService;


    @GetMapping("/student")
    public ResponseEntity<DataResponse> mainStudentInfo(HttpServletRequest http) {
        String token = tokenService.getTokenFromRequest(http);
        String studentId = tokenService.getStudentIdFromToken(token);

        MainDto.StudentInfoResponse studentInfoResponse = mainService.studentInfo(studentId);

        return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, studentInfoResponse));
    }

    @GetMapping("/schedule")
    public ResponseEntity<DataResponse> mainScheduleInfo(HttpServletRequest http) {
        String token = tokenService.getTokenFromRequest(http);
        String studentId = tokenService.getStudentIdFromToken(token);

        List<ScheduleInfoDto> scheduleInfo = mainService.scheduleInfo(studentId);

        return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, scheduleInfo));
    }

}
