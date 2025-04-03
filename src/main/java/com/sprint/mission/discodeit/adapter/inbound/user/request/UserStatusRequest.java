package com.sprint.mission.discodeit.adapter.inbound.user.request;

import java.time.Instant;

public record UserStatusRequest(
    Instant newLastActiveAt
) {

}
