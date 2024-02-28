package sejong.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.user.global.res.BaseResponse;
import sejong.user.global.res.DataResponse;
import sejong.user.service.TokenService;
import sejong.user.service.UserService;
import sejong.user.service.dto.UserDto;

import java.io.IOException;

import static sejong.user.global.res.constant.StatusCodeConstant.OK_STATUS_CODE;
import static sejong.user.global.res.constant.ResponseMessageConstant.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service/users")
public class UserController {
    private final TokenService tokenService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<DataResponse> getMyPage(HttpServletRequest http) {
        String token = tokenService.getTokenFromRequest(http);
        String studentId = tokenService.getStudentIdFromToken(token);
        UserDto.MyPageResponse response = userService.getMyPage(studentId);

        return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, response));
    }

    @PutMapping()
    public ResponseEntity<BaseResponse> modifyUser(HttpServletRequest http,
                                                    @ModelAttribute UserDto.ModifyUserRequest modifyUserDto) throws IOException {
        String token = tokenService.getTokenFromRequest(http);
        String studentId = tokenService.getStudentIdFromToken(token);

        userService.modifyUser(studentId, modifyUserDto);

        return ResponseEntity.ok().body(new BaseResponse(OK_STATUS_CODE, SUCCESS));
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> logout(HttpServletRequest http) {
        String token = tokenService.getTokenFromRequest(http);
        String studentId = tokenService.getStudentIdFromToken(token);

        userService.logout(studentId);

        return ResponseEntity.ok().body(new BaseResponse(OK_STATUS_CODE, SUCCESS));
    }
}
