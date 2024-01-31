package sejong.user.service.dto;

import lombok.Getter;
import lombok.Setter;

public class UserAuthDto {

    @Getter
    @Setter
    public static class UserAuthRequest {
        private String id;
        private String pw;
    }
}
