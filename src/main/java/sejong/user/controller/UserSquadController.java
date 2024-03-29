package sejong.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.user.controller.res.UserSquadResponse;
import sejong.user.service.UserSquadService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-service")
public class UserSquadController {
    private final UserSquadService userSquadService;
    @GetMapping("/users/{userId}/squad")
    public ResponseEntity<UserSquadResponse> getUserInfoInSquad(@PathVariable(name = "userId") Long userId){
        UserSquadResponse userSquadResponse = userSquadService.getUserInfoInSquad(userId);
        return ResponseEntity.ok().body(userSquadResponse);
    }
}
