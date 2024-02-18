package sejong.user.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public class UserDto {

    @Getter
    @Builder
    public static class MyPageResponse {
        public String studentId;
        public String name;
        public String image;
    }

    @Getter
    @Builder
    public static class ModifyUserRequest {
        public String name;
        public Integer age;
        public Integer height;
        public String sex;
        public String position;
        public String foot;
        public MultipartFile image;
    }
}
