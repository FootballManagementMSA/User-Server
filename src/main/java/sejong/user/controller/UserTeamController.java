package sejong.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.user.controller.res.SizeUserTeamResponse;
import sejong.user.service.UserTeamService;
import sejong.user.service.dto.SizeUserTeamDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-service")
public class UserTeamController {
    private final UserTeamService userTeamService;
    @GetMapping("/users/teams/{teamId}")
    public ResponseEntity<SizeUserTeamResponse> countUsersInTeam(@PathVariable Long teamId){
        SizeUserTeamDto dto = userTeamService.countUserTeam(teamId);
        return ResponseEntity.ok().body(SizeUserTeamResponse.of(dto.getSize()));
    }
}
