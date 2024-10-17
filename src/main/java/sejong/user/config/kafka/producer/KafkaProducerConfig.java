package sejong.user.config.kafka.producer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import sejong.user.config.Json.CommonJsonSerializer;

@Configuration
@EnableKafka
public class KafkaProducerConfig {
	@Value("${spring.kafka.producer.bootstrap-servers}")
	private String BOOTSTRAP_SERVERS_CONFIG;

	@Bean
	public Map<String, Object> UserProducerConfig() {
		return CommonJsonSerializer.getStringObjectMap(BOOTSTRAP_SERVERS_CONFIG);
	}

	@Bean
	public ProducerFactory<String, String> deleteUserProducerFactory() {
		return new DefaultKafkaProducerFactory<>(UserProducerConfig());
	}

	@Bean
	public KafkaTemplate<String, String> deleteUserKafkaTemplate() {
		return new KafkaTemplate<>(deleteUserProducerFactory());
	}

	@Bean
	public StringJsonMessageConverter jsonConverter() {
		return new StringJsonMessageConverter();
	}
}
