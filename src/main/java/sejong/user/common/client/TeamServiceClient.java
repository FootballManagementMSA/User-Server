package sejong.user.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import sejong.user.common.client.dto.ScheduleInfoDto;
import sejong.user.service.dto.MainDto;

import java.util.List;

@FeignClient(name = "team-service")
public interface TeamServiceClient {
    @GetMapping("/api/team-service/{userId}/schedule")
    ResponseEntity<List<ScheduleInfoDto>> getScheduleInfo(@PathVariable(value = "userId") Long userId);

    @GetMapping("/api/team-service/team-info")
    ResponseEntity<List<MainDto.RegisteredTeamInfoResponse>> getRegisteredTeamInfo(@RequestBody List<Long> teamIds);
}
