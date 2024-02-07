package sejong.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.user.global.res.BaseResponse;
import sejong.user.global.res.DataResponse;
import sejong.user.service.dto.UserAuthDto;
import sejong.user.service.UserAuthService;

import static sejong.user.global.res.ResponseMessageConstant.SUCCESS;
import static sejong.user.global.res.StatusCodeConstant.OK_STATUS_CODE;

@RestController
@RequestMapping("/api/user-service")
public class UserAuthController {
    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<DataResponse> login(@RequestBody UserAuthDto.UserAuthRequest userAuthLoginRequest) {
        UserAuthDto.UserLoginResponse userLoginResponse = userAuthService.userLogin(userAuthLoginRequest);

        return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, userLoginResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> registerUser(@RequestBody UserAuthDto.UserRegisterRequest userRegisterRequest){
        userAuthService.registerUser(userRegisterRequest);

        return ResponseEntity.ok().body(new BaseResponse(OK_STATUS_CODE, SUCCESS));
    }
}
