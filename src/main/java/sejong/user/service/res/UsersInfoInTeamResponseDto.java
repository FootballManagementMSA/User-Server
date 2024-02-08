package sejong.user.service.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UsersInfoInTeamResponseDto {
    private Long userId;
    private String userName;
    private String position;
    private Integer age;
    private Integer teamCnt;
}
