package sejong.user.user.dto;

import lombok.Builder;
import lombok.Getter;

public class UserDto {

    @Getter
    @Builder
    public static class GetUserResponse {
        public String studentId;
        public String name;
    }

    @Getter
    @Builder
    public static class ModifyUserRequest {
        public String studentId;
        public String name;
        public Integer age;
        public Integer height;
        public String sex;
        public String position;
        public String foot;
    }
}
