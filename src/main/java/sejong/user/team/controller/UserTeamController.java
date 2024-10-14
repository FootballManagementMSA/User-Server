package sejong.user.team.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.user.team.controller.response.IncludeOwnerInTeamDto;
import sejong.user.team.controller.response.SizeUserTeamResponse;
import sejong.user.global.response.DataResponse;
import sejong.user.team.service.UserTeamService;
import sejong.user.team.dto.SizeUserTeamDto;
import sejong.user.team.dto.UserTeamInfoDto;
import sejong.user.team.service.response.ApplyUsersInfoResponseDto;
import sejong.user.team.service.response.UsersInfoInTeamResponseDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-service")
public class UserTeamController {
    private final UserTeamService userTeamService;

    @GetMapping("/users/teams/{teamId}/size")
    public ResponseEntity<SizeUserTeamResponse> countUsersInTeam(@PathVariable Long teamId) {
        SizeUserTeamDto dto = userTeamService.countUserTeam(teamId);
        return ResponseEntity.ok().body(SizeUserTeamResponse.of(dto.getSize()));
    }

    /**
     * 팀에 속한 사용자 리스트 조회 -> 수정해야함
     * @param teamId
     * @return
     */
    @GetMapping("/users/teams/{teamId}")
    public DataResponse<List<UsersInfoInTeamResponseDto>> UsersInTeam(@PathVariable Long teamId) {
        return new DataResponse(userTeamService.findMembersInfoInTeam(teamId));
    }

    @GetMapping("/users/teams/{teamId}/apply")
    public DataResponse<List<ApplyUsersInfoResponseDto>> applicantInTeam(@PathVariable Long teamId) {
        return new DataResponse(userTeamService.findApplicantInTeam(teamId));
    }
    /**
     * 팀 생성 시 생성자(동아리장)가 팀에 바로 포함되도록 호출하는 API
     */
    @PostMapping("/users/teams/")
    public DataResponse includeOwnerInTeam(@RequestBody IncludeOwnerInTeamDto includeOwnerInTeamDto){
        userTeamService.includeOwnerInTeam(includeOwnerInTeamDto.getTeamId(),includeOwnerInTeamDto.getToken());
        return new DataResponse<>();
    }

    @GetMapping("/users/{userId}/teams")
    public ResponseEntity<List<UserTeamInfoDto>> findUserTeams(@PathVariable Long userId){
        return ResponseEntity.ok(userTeamService.findUserTeams(userId));
    }
}
