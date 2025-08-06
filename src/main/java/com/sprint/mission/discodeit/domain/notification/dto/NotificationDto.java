package com.sprint.mission.discodeit.domain.notification.dto;

import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record NotificationDto(
    UUID id,
    Instant createdAt,
    UUID receiverId,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

  public static NotificationDto from(Notification notification) {
    return NotificationDto.builder()
        .id(notification.getId())
        .createdAt(notification.getCreatedAt())
        .receiverId(notification.getReceiverId())
        .title(notification.getTitle())
        .content(notification.getContent())
        .type(notification.getType())
        .targetId(notification.getTargetId())
        .build();
  }
} 