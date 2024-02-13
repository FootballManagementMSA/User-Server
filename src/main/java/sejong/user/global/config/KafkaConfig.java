package sejong.user.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import sejong.user.global.kafka.CommonJsonDeserializer;
import sejong.user.service.req.ApplyUserTeamInfoRequestDto;
import sejong.user.service.req.ConfirmApplicationRequestDto;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${kafka.BOOTSTRAP_SERVERS_CONFIG}")
    private String BOOTSTRAP_SERVERS_CONFIG;

    @Value("${kafka.GROUP_ID_CONFIG}")
    private String GROUP_ID_CONFIG;
    /*@Bean
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
    }*/

    @Bean
    public Map<String, Object> consumerConfigs() {
        return CommonJsonDeserializer.getStringObjectMap(BOOTSTRAP_SERVERS_CONFIG,GROUP_ID_CONFIG);
    }

    @Bean
    public ConsumerFactory<String, ApplyUserTeamInfoRequestDto> ApplyUserTeamInfoRequestDto_ConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConsumerFactory<String, ConfirmApplicationRequestDto> ConfirmApplicationRequestDto_ConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApplyUserTeamInfoRequestDto> ApplyUserTeamListener() {
        ConcurrentKafkaListenerContainerFactory<String, ApplyUserTeamInfoRequestDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ApplyUserTeamInfoRequestDto_ConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ConfirmApplicationRequestDto> confirmApplicantListener() {
        ConcurrentKafkaListenerContainerFactory<String, ConfirmApplicationRequestDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ConfirmApplicationRequestDto_ConsumerFactory());
        return factory;
    }

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }
}
