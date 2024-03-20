package sejong.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sejong.user.global.res.DataResponse;
import sejong.user.service.FcmTokenService;
import sejong.user.service.dto.FcmTokenDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service/fcm")
public class FcmTokenController {
    private final FcmTokenService fcmTokenService;
    @PostMapping("/token")
    public DataResponse saveOrUpdateToken(@RequestBody FcmTokenDto fcmTokenDto) {
        fcmTokenService.saveOrUpdateToken(fcmTokenDto);
        return new DataResponse<>();
    }
    @GetMapping("/")
    public String getFcmToken(@RequestParam Long teamId){
        return fcmTokenService.getFcmToken(teamId);
    }
}
