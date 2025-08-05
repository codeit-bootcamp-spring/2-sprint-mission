package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final SseService sseService;

    public void publishNotification(UUID userId, String message) {
        sseService.notify(userId, "notifications", Map.of("message", message));
    }

    public void publishFileUploadStatus(UUID userId, BinaryContentDto binaryContentDto) {
        sseService.notify(userId, "binaryContents.status", binaryContentDto);
    }

    public void publishChannelRefresh(UUID channelId) {
        sseService.broadcast("channels.refresh", Map.of("channelId", channelId));
    }

    public void publishUserRefresh(UUID userId) {
        sseService.broadcast("users.refresh", Map.of("userId", userId));
    }
} 