package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record AsyncTaskFailureEvent(
    String taskName,
    String requestId,
    String failureReason
) {

}