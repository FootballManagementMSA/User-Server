package sejong.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.user.service.dto.UserAuthDto;
import sejong.user.service.UserAuthService;

@RestController
@RequestMapping("/api/user-service")
public class UserAuthController {
    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> getUser(@RequestBody UserAuthDto.UserAuthRequest userAuthLoginRequest) {
        userAuthService.userLogin(userAuthLoginRequest);

        return ResponseEntity.noContent().build();
    }
}
