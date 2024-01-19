package sejong.user.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SizeOfUsersInTeamDto {
    private Integer size;

    public static SizeOfUsersInTeamDto of(int size) {
        return SizeOfUsersInTeamDto.builder()
                .size(size)
                .build();
    }

}
