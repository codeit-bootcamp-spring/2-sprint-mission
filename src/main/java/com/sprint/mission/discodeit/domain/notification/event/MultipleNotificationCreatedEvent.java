package com.sprint.mission.discodeit.domain.notification.event;

import java.util.Set;
import java.util.UUID;

public record MultipleNotificationCreatedEvent(Set<UUID> receiverIds) {

}