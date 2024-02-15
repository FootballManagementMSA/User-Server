package sejong.user.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @Getter
    @Builder
    public static class MyPageResponse {
        public String studentId;
        public String name;
        public String image;
    }
}
