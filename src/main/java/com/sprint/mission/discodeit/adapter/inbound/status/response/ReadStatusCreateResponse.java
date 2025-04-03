package com.sprint.mission.discodeit.adapter.inbound.status.response;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "ReadStatus Create")
public record ReadStatusCreateResponse(
    @Schema(description = "ReadStatus Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID readStatusId,
    @Schema(description = "ReadStatus 생성 시각", example = "2025-04-03T01:49:44.983Z")
    Instant createdAt,
    @Schema(description = "ReadStatus 수정 시각", example = "2025-04-03T01:49:44.983Z")
    Instant updatedAt,
    @Schema(description = "User Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID userId,
    @Schema(description = "Channel Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID channelId,
    @Schema(description = "최근 읽은 시각", example = "2025-04-03T01:49:44.983Z")
    Instant lastReadAt
) {

  public static ReadStatusCreateResponse create(ReadStatus readStatus) {
    return ReadStatusCreateResponse.builder()
        .readStatusId(readStatus.getReadStatusId())
        .createdAt(readStatus.getCreatedAt())
        .updatedAt(readStatus.getUpdatedAt())
        .userId(readStatus.getUserId())
        .channelId(readStatus.getChannelId())
        .lastReadAt(readStatus.getLastReadAt())
        .build();
  }
}
