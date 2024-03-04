package sejong.user.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import sejong.user.global.kafka.CommonJsonDeserializer;
import sejong.user.global.kafka.CommonJsonSerializer;
import sejong.user.service.req.ApplyUserTeamInfoRequestDto;
import sejong.user.service.req.ConfirmApplicationRequestDto;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${kafka.BOOTSTRAP_SERVERS_CONFIG}")
    private String BOOTSTRAP_SERVERS_CONFIG;

    @Value("${spring.kafka.consumer.group-id}")
    private String GROUP_ID_CONFIG;

    // -->
    @Bean
    public Map<String, Object> consumerConfigs() {
        return CommonJsonDeserializer.getStringObjectMap(BOOTSTRAP_SERVERS_CONFIG, GROUP_ID_CONFIG);
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
    // <-- Consumer Config

    // -->
    @Bean
    public Map<String, Object> UserProducerConfig() {
        return CommonJsonSerializer.getStringObjectMap(BOOTSTRAP_SERVERS_CONFIG);
    }

    @Bean
    public ProducerFactory<String, Void> deleteSquadProducerFactory() {
        return new DefaultKafkaProducerFactory<>(UserProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, Void> deleteSquadKafkaTemplate(){
        return new KafkaTemplate<>(deleteSquadProducerFactory());
    }
    // <-- Producer Config

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }
}
