package com.sprint.mission.discodeit.dto.notification;

import com.sprint.mission.discodeit.constant.NotificationType;
import java.util.UUID;

public record NotificationCreateDto (
    UUID receiverId,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

}
