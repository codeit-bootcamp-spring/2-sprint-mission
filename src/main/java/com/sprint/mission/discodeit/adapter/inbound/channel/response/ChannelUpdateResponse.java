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
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    ChannelType type,
    String name,
    String description
) {

  public static ChannelUpdateResponse create(Channel channel) {
    return ChannelUpdateResponse.builder()
        .id(channel.getChannelId())
        .createdAt(channel.getCreatedAt())
        .updatedAt(channel.getUpdatedAt())
        .type(channel.getType())
        .name(channel.getName()).build();
  }
}
