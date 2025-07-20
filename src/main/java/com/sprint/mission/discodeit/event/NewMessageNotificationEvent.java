package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import java.util.UUID;

public record NewMessageNotificationEvent(
    UUID receiverId,
    UUID channelId,
    UUID messageId,
    String authorName,
    String requestId
) {

}
