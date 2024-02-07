package sejong.user.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserAuthDto {

    @Getter
    @Setter
    @Builder
    public static class UserAuthRequest {
        private String id;
        private String pw;
    }

    @Getter
    @Setter
    public static class UserRegisterRequest{
        private String studentId;
        private String password;
        private String position;
        private String foot;
        private String sex;
        private Integer age;
        private Integer height;
    }

    @Getter
    @Setter
    @Builder
    public static class UserDto{
        private String studentId;
        private String password;
        private String grade;
        private String status;
        private String major;
        private String name;

        private String position;
        private String foot;
        private String sex;
        private Integer age;
        private Integer height;
    }

    @Getter
    @Setter
    @Builder
    public static class UserLoginResponse {
        private String accessToken;
        private String refreshToken;
    }
}
