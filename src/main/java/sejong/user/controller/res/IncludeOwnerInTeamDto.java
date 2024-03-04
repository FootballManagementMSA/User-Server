package sejong.user.controller.res;

import lombok.Getter;

@Getter
public class IncludeOwnerInTeamDto {
    private Long teamId;
    private String token;
}
