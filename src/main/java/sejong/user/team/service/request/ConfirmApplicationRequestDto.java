package sejong.user.team.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ConfirmApplicationRequestDto {
    private boolean approve;
    private Long teamId;
    private Long userId;
}
