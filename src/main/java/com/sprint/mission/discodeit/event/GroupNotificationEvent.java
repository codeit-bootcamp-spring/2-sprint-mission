package com.sprint.mission.discodeit.event;

import java.util.Set;
import java.util.UUID;

public record GroupNotificationEvent(
    Set<UUID> receiverIds
) {

}
