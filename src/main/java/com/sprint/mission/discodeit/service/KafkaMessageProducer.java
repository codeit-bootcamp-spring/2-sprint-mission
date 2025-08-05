package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String MESSAGE_TOPIC = "chat-messages";
    private static final String NOTIFICATION_TOPIC = "notifications";
    private static final String CHANNEL_TOPIC = "channel-events";
    private static final String FILE_STATUS_TOPIC = "file-status";

    public void sendMessage(MessageDto messageDto) {
        try {
            String key = "channel-" + messageDto.channelId();
            kafkaTemplate.send(MESSAGE_TOPIC, key, messageDto);
            log.info("메시지 Kafka 전송 완료: 채널 {}, 메시지 {}", messageDto.channelId(), messageDto.id());
        } catch (Exception e) {
            log.error("메시지 Kafka 전송 실패: {}", e.getMessage(), e);
        }
    }

    public void sendNotification(Object notificationData) {
        try {
            kafkaTemplate.send(NOTIFICATION_TOPIC, notificationData);
            log.info("알림 Kafka 전송 완료: {}", notificationData);
        } catch (Exception e) {
            log.error("알림 Kafka 전송 실패: {}", e.getMessage(), e);
        }
    }

    public void sendChannelEvent(Object channelEvent) {
        try {
            kafkaTemplate.send(CHANNEL_TOPIC, channelEvent);
            log.info("채널 이벤트 Kafka 전송 완료: {}", channelEvent);
        } catch (Exception e) {
            log.error("채널 이벤트 Kafka 전송 실패: {}", e.getMessage(), e);
        }
    }

    public void sendFileStatusEvent(Object fileStatusEvent) {
        try {
            kafkaTemplate.send(FILE_STATUS_TOPIC, fileStatusEvent);
            log.info("파일 상태 이벤트 Kafka 전송 완료: {}", fileStatusEvent);
        } catch (Exception e) {
            log.error("파일 상태 이벤트 Kafka 전송 실패: {}", e.getMessage(), e);
        }
    }
} 