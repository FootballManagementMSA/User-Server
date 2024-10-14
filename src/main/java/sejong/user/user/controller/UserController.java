package sejong.user.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.user.global.response.BaseResponse;
import sejong.user.global.response.DataResponse;
import sejong.user.token.service.TokenService;
import sejong.user.user.service.UserService;
import sejong.user.user.dto.UserDto;

import java.io.IOException;

import static sejong.user.global.response.constant.StatusCodeConstant.OK_STATUS_CODE;
import static sejong.user.global.response.constant.ResponseMessageConstant.SUCCESS;

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

    @DeleteMapping()
    public ResponseEntity<BaseResponse> deleteUser(HttpServletRequest http) {
        String token = tokenService.getTokenFromRequest(http);
        String studentId = tokenService.getStudentIdFromToken(token);

        userService.deleteUser(studentId);

        return ResponseEntity.ok().body(new BaseResponse(OK_STATUS_CODE, SUCCESS));
    }
}
