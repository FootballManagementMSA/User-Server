package sejong.user.token.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmTokenDto {
    private Long userId;
    private String fcmToken;
}
