package sejong.user.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "team-service")
public interface TeamServiceClient {
    @DeleteMapping("/api/team-service/{userId}/squad")
    ResponseEntity<Void> deleteUserSquad(@PathVariable("userId") Long userId);
}
