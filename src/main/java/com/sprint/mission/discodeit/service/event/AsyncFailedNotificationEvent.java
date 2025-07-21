package com.sprint.mission.discodeit.service.event;

import java.util.UUID;

public record AsyncFailedNotificationEvent(UUID userId, String taskName, String failureReason) {

}
