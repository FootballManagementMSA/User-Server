package sejong.user.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

public class MainDto {

    @Getter
    @Builder
    public static class StudentInfoResponse {
        private String name;
        private Integer game;
        private Integer goal;
        private String position;
        private String foot;
        private Integer age;
    }

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RegisteredTeamInfoResponse {
        private Long id;
        private String uniqueNum;
    }
}
