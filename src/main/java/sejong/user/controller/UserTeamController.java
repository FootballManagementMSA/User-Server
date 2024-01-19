package sejong.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sejong.user.controller.res.SizeOfUsersInTeamResponse;
import sejong.user.service.UserTeamService;
import sejong.user.service.dto.SizeOfUsersInTeamDto;

@RequiredArgsConstructor
@RestController("/api/user-service")
public class UserTeamController {
    private final UserTeamService userTeamService;
    @GetMapping("/users/teams/{teamId}")
    public ResponseEntity<SizeOfUsersInTeamResponse> findUsersInTeam(@PathVariable Long teamId){
        SizeOfUsersInTeamDto dto = userTeamService.findUsersInTeam(teamId);
        return ResponseEntity.ok().body(SizeOfUsersInTeamResponse.of(dto.getSize()));
    }
}
