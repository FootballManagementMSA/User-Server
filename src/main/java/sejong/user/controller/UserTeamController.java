package sejong.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.user.controller.res.IncludeOwnerInTeamDto;
import sejong.user.controller.res.SizeUserTeamResponse;
import sejong.user.global.res.DataResponse;
import sejong.user.service.UserTeamService;
import sejong.user.service.dto.SizeUserTeamDto;
import sejong.user.service.res.ApplyUsersInfoResponseDto;
import sejong.user.service.res.UsersInfoInTeamResponseDto;

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
}
