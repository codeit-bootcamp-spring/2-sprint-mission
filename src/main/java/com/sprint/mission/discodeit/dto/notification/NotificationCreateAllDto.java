package com.sprint.mission.discodeit.dto.notification;

import com.sprint.mission.discodeit.constant.NotificationType;
import java.util.Set;
import java.util.UUID;

public record NotificationCreateAllDto(
    Set<UUID> receiverIds,
    String title,
    String content,
    NotificationType type,
    UUID channelId
) {

}
