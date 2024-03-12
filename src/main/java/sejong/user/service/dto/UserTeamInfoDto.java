package sejong.user.service.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import sejong.user.entity.Role;

@Builder
@Getter
public class UserTeamInfoDto {
    private Long teamId;
    private String role;
    private String introduce;
}
