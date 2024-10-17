package sejong.user.user.kafka.producer;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaProducer {
	private final KafkaTemplate<String, String> deleteUserKafkaTemplate;

	public void deleteUser(String studentId) {
		deleteUserKafkaTemplate.send("user-delete", studentId);
		System.out.println("Sent delete user message for studentId: " + studentId); // 로그 추가
	}
}
