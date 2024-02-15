package sejong.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.user.global.res.DataResponse;
import sejong.user.service.TokenService;
import sejong.user.service.UserService;
import sejong.user.service.dto.UserDto;

import static sejong.user.global.exception.constant.StatusCodeConstant.OK_STATUS_CODE;
import static sejong.user.global.res.constant.ResponseMessageConstant.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service")
public class UserController {
    private final TokenService tokenService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<DataResponse> getMyPage(HttpServletRequest http){
        UserDto.MyPageResponse response = userService.getMyPage(tokenService.getStudentIdFromToken(http));

        return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, response));
    }
}
