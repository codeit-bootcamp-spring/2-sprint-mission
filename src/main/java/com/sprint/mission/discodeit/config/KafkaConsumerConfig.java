package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.dto.data.ChatMessageKafkaEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, ChatMessageKafkaEvent> chatMessageConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            "localhost:9092"); // or ${KAFKA_BOOTSTRAP_SERVERS}
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "chat-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            new JsonDeserializer<>(ChatMessageKafkaEvent.class)
        );
    }

    @Bean(name = "chatKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ChatMessageKafkaEvent> chatKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatMessageKafkaEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(chatMessageConsumerFactory());
        return factory;
    }
}

