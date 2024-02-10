package sejong.user.global.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import sejong.user.service.req.ApplyUserInfoRequestDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${kafka.BOOTSTRAP_SERVERS_CONFIG}")
    private String BOOTSTRAP_SERVERS_CONFIG;

    @Value("${kafka.GROUP_ID_CONFIG}")
    private String GROUP_ID_CONFIG;
    @Bean
    public ConsumerFactory<String, ApplyUserInfoRequestDto> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);

        return new DefaultKafkaConsumerFactory<>(config,new StringDeserializer(),new JsonDeserializer<>(ApplyUserInfoRequestDto.class,false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApplyUserInfoRequestDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ApplyUserInfoRequestDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
