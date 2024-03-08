package sejong.user.common.client.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleInfoDto {
    private String place;
    private LocalDateTime startTime;
    private HomeTeam homeTeam;
    private AwayTeam awayTeam;

    @Getter
    @Builder
    public static class HomeTeam {
        private String name;
        private String emblem;
    }

    @Getter
    @Builder
    public static class AwayTeam {
        private String name;
        private String emblem;
    }
}
