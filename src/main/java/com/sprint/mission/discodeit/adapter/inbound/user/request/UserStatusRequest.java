package com.sprint.mission.discodeit.adapter.inbound.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "User Status Update")
public record UserStatusRequest(
    @Schema(description = "User Status new LastActiveAt", example = "2025-04-03T01:38:38.006Z")
    Instant newLastActiveAt
) {

}
