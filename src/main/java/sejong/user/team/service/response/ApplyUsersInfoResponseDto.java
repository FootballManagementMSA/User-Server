package sejong.user.team.service.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApplyUsersInfoResponseDto {
    private Long userId;
    private String userName;
    private String position;
    private Integer age;
    private Integer teamCnt;
    private String introduce;
}
