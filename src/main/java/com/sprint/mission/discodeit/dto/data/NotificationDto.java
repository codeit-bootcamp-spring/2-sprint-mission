package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.NotificationType;

import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        Instant createdAt,
        UUID receiverId,           // 알림을 받을 User 의 ID
        String title,
        String content,
        NotificationType type,
        UUID targetId              // optional
) {}