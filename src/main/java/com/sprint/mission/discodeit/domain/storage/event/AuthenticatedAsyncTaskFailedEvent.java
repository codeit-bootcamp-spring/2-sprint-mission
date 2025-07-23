package com.sprint.mission.discodeit.domain.storage.event;

import java.util.UUID;

public record AuthenticatedAsyncTaskFailedEvent(
    AsyncTaskFailedEvent asyncTaskFailedEvent,
    UUID authenticatedUserId
) {

}