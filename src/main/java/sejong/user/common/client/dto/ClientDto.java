package sejong.user.common.client.dto;

import lombok.Builder;
import lombok.Getter;

public class ClientDto {

    @Getter
    @Builder
    public static class UserSquadClientRequest {
        Long userId;
    }
}
