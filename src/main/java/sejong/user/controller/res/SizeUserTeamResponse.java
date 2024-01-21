package sejong.user.controller.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SizeUserTeamResponse {
    private Integer size;
    public static SizeUserTeamResponse of(Integer num){
        return SizeUserTeamResponse.builder()
                .size(num)
                .build();
    }
}
