package sejong.user.team.controller.response;

import lombok.Getter;

@Getter
public class IncludeOwnerInTeamDto {
    private Long teamId;
    private String token;
}
