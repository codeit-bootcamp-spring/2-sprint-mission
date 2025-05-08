package com.sprint.mission.discodeit.core.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "User Status Update")
public record UserStatusRequest(
    Instant newLastActiveAt
) {

}
