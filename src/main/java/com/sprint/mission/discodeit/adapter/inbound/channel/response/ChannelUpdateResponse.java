package com.sprint.mission.discodeit.adapter.inbound.channel.response;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Channel 정보가 성공적으로 수정됨")
public record ChannelUpdateResponse(
    @Schema(description = "Channel Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,
    @Schema(description = "Channel 생성 시각", example = "2025-04-03T01:49:44.983Z")
    Instant createdAt,
    @Schema(description = "Channel 수정 시각", example = "2025-04-03T01:49:44.983Z")
    Instant updatedAt,
    @Schema(description = "Channel Type", example = "PUBLIC")
    ChannelType type,
    @Schema(description = "Channel name", example = "string")
    String name,
    @Schema(description = "Channel description", example = "string")
    String description
) {

  public static ChannelUpdateResponse create(Channel channel) {
    return ChannelUpdateResponse.builder()
        .id(channel.getId())
        .createdAt(channel.getCreatedAt())
        .updatedAt(channel.getUpdatedAt())
        .type(channel.getType())
        .name(channel.getName()).build();
  }
}
