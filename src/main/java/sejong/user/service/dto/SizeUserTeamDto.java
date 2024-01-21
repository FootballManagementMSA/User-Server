package sejong.user.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SizeUserTeamDto {
    private Integer size;

    public static SizeUserTeamDto of(int size) {
        return SizeUserTeamDto.builder()
                .size(size)
                .build();
    }

}
