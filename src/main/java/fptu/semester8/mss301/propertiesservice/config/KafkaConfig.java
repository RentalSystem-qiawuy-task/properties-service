package fptu.semester8.mss301.propertiesservice.config;

import org.apache.kafka.common.KafkaException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration(proxyBeanMethods = false)
public class KafkaConfig {
    /**
     * Retries a failed record three times. Without an agreed event contract and
     * provisioned dead-letter topic, keep retrying after recovery fails instead
     * of silently acknowledging and dropping the record.
     */
    @Bean
    DefaultErrorHandler kafkaDefaultErrorHandler() {
        return new DefaultErrorHandler((record, exception) -> {
            throw new KafkaException("Kafka record processing failed; no dead-letter topic is configured", exception);
        }, new FixedBackOff(1_000L, 3L));
    }
}
