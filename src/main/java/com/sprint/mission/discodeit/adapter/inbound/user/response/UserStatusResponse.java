package com.sprint.mission.discodeit.adapter.inbound.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "User Status Response")
public record UserStatusResponse(
    @Schema(description = "User Status Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,

    @Schema(description = "User Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID userId,

    @Schema(description = "User Status 활성 시각", example = "2025-04-03T01:38:38.006Z")
    Instant lastActiveAt

) {

}