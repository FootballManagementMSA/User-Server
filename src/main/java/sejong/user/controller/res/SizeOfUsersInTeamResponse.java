package sejong.user.controller.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SizeOfUsersInTeamResponse {
    private Integer size;
    public static SizeOfUsersInTeamResponse of(Integer num){
        return SizeOfUsersInTeamResponse.builder()
                .size(num)
                .build();
    }
}
