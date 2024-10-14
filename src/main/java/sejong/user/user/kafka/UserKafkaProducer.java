package sejong.user.user.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaProducer {
    private final KafkaTemplate<String, Long> deleteUserKafkaTemplate;

    public void deleteUser(Long userId) {
        deleteUserKafkaTemplate.send("user", userId);
    }
}
