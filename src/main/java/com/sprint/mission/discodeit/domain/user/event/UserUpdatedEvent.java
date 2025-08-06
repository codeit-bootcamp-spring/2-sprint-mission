package com.sprint.mission.discodeit.domain.user.event;

import java.util.UUID;

public record UserUpdatedEvent(
    UUID userId
) {

}
