package com.sprint.mission.discodeit.dto.data;

import java.util.Map;

public record NotificationMessage(
    String eventName,        // 예: channels.refresh
    String userIdOrKey,      // 예: userId, binaryContentId 등
    Map<String, Object> data
) {

}
