package sejong.user.config.kafka.consumer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import sejong.user.config.Json.CommonJsonDeserializer;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
	@Value("${spring.kafka.consumer.bootstrap-servers}")
	private String BOOTSTRAP_SERVERS_CONFIG;

	@Value("${spring.kafka.consumer.group-id}")
	private String GROUP_ID_CONFIG;

	@Bean
	public Map<String, Object> consumerConfigs() {
		return CommonJsonDeserializer.getStringObjectMap(BOOTSTRAP_SERVERS_CONFIG, GROUP_ID_CONFIG);
	}

	@Bean
	public ConsumerFactory<String, String> stringValueConsumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> ApplyStringValueListener() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(stringValueConsumerFactory());
		return factory;
	}
}
