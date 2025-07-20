package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record AsyncFailedNotificationEvent(
    UUID receiverId,
    String taskName,
    String requestId,
    String failureReason
) {

}
