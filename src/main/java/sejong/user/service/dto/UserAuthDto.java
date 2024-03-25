package sejong.user.service.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

public class UserAuthDto {

    @Getter
    @Builder
    public static class UserAuthRequest {
        private String id;
        private String pw;
    }

    @Getter
    @Builder
    public static class UserRegisterRequest{
        private String studentId;
        private String password;
        private String position;
        private String foot;
        private String sex;
        private Integer age;
        private Integer height;
        private MultipartFile image;
    }

    @Getter
    @Builder
    public static class UserDto{
        private String studentId;
        private String password;
        private String grade;
        private String status;
        private String major;
        private String name;
        private String image;

        private String position;
        private String foot;
        private String sex;
        private Integer age;
        private Integer height;
    }

    @Getter
    @Builder
    public static class UserLoginResponse {
        private Long userId;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueTokenRequest {
        private String refreshToken;
    }

    @Getter
    @Builder
    public static class ReissueTokenResponse {
        private String accessToken;
        private String refreshToken;
    }
}
