package sejong.user.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sejong.user.common.client.dto.ScheduleInfoDto;

import java.util.List;

@FeignClient(name = "team-service")
public interface TeamServiceClient {
    @GetMapping("/api/team-service/{userId}/schedule")
    ResponseEntity<List<ScheduleInfoDto>> getScheduleInfo(@PathVariable(value = "userId") Long userId);
}
