package com.sprint.mission.discodeit.dto.event;

import java.util.UUID;

public record NotificationAsyncFailedEvent(
    String taskName,
    UUID requestId,
    String failureReason
) {

}
