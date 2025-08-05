package com.sprint.mission.discodeit.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.dto.data.NotificationMessage;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseKafkaConsumer {

    private final SseService sseService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "sse-events", groupId = "sse-group")
    public void consume(String message) {
        try {
            NotificationMessage payload = objectMapper.readValue(message,
                NotificationMessage.class);
            switch (payload.eventName()) {
                case "channels.refresh" -> sseService.notifyChannelRefresh(payload.userIdOrKey(),
                    (String) payload.data().get("channelId"));
                case "users.refresh" -> sseService.notifyUserRefresh(payload.userIdOrKey(),
                    (String) payload.data().get("userId"));
                case "notifications" -> {
                    NotificationDto dto = objectMapper.convertValue(payload.data(),
                        NotificationDto.class);
                    sseService.notifyNewNotification(payload.userIdOrKey(), dto);
                }
                case "binaryContents.status" -> {
                    BinaryContentDto dto = objectMapper.convertValue(payload.data(),
                        BinaryContentDto.class);
                    sseService.notifyBinaryContentStatus(payload.userIdOrKey(), dto);
                }
                // 필요 시 다른 이벤트 처리 추가
            }
        } catch (Exception e) {
            log.error("SSE Kafka 메시지 처리 중 오류", e);
        }
    }
}
