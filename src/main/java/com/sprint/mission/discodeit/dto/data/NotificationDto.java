package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class NotificationDto {
    private final UUID id;
    private final NotificationType type;
    private final String title;
    private final UUID receiverId;
    private final UUID targetId;
    private final String content;
    private final Instant createdAt;

    public static NotificationDto from(Notification notification) {
        return NotificationDto.builder()
            .id(notification.getId())
            .type(notification.getType())
            .title(notification.getTitle())
            .receiverId(notification.getReceiver().getId())
            .targetId(notification.getTargetId())
            .content(notification.getContent())
            .createdAt(notification.getCreatedAt())
            .build();
    }
} 