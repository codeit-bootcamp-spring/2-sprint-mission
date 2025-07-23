package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.NotificationType;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
    @NotNull
    UUID id,
    @NotNull
    Instant createdAt,
    @NotNull
    UUID receiverId,
    @NotNull
    String title,
    @NotNull
    String content,
    @NotNull
    NotificationType type,

    UUID targetId
) {

}
