package sejong.user.service.dto;

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
    }
}
