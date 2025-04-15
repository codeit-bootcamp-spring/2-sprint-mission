package com.sprint.mission.discodeit.adapter.inbound.status.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "ReadStatus Create")
public record ReadStatusResponse(
    @Schema(description = "ReadStatus Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,
    @Schema(description = "User Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID userId,
    @Schema(description = "Channel Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID channelId,
    @Schema(description = "최근 읽은 시각", example = "2025-04-03T01:49:44.983Z")
    Instant lastReadAt
) {

}
