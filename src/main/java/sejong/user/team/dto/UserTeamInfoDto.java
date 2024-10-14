package sejong.user.team.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserTeamInfoDto {
    private Long teamId;
    private String role;
    private String introduce;
}
