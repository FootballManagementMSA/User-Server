package sejong.user.service.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyUserTeamInfoRequestDto {
    private Long teamId;
    private Long userId;
    private String introduce;
}
