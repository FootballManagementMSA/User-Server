package sejong.user.team.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.user.global.response.BaseResponse;
import sejong.user.global.response.DataResponse;
import sejong.user.user.dto.UserAuthDto;
import sejong.user.user.service.UserAuthService;

import java.io.IOException;

import static sejong.user.global.response.constant.ResponseMessageConstant.SUCCESS;
import static sejong.user.global.response.constant.StatusCodeConstant.OK_STATUS_CODE;

@RestController
@RequestMapping("/api/user-service")
@RequiredArgsConstructor
public class UserAuthController {
    private final UserAuthService userAuthService;

    @PostMapping("/login")
    public ResponseEntity<DataResponse> login(@RequestBody UserAuthDto.UserAuthRequest userAuthLoginRequest) {
        UserAuthDto.UserLoginResponse userLoginResponse = userAuthService.userLogin(userAuthLoginRequest);

        return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, userLoginResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> registerUser(@ModelAttribute UserAuthDto.UserRegisterRequest userRegisterRequest) throws IOException {
        userAuthService.registerUser(userRegisterRequest);

        return ResponseEntity.ok().body(new BaseResponse(OK_STATUS_CODE, SUCCESS));
    }

    @PostMapping()
    public ResponseEntity<DataResponse> reissueToken(@RequestBody UserAuthDto.ReissueTokenRequest reissueTokenRequest) {
        UserAuthDto.ReissueTokenResponse reissueTokenResponse = userAuthService.reissueToken(reissueTokenRequest);

        return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, reissueTokenResponse));
    }
}
