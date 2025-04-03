package com.sprint.mission.discodeit.adapter.inbound.channel.response;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Public Channel 성공적으로 생성됨")
public record ChannelCreateResponse(
//    boolean success,
//    String message
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    ChannelType type,
    String name,
    String description
) {

  public static ChannelCreateResponse create(Channel channel) {
    return ChannelCreateResponse.builder()
        .id(channel.getChannelId())
        .createdAt(channel.getCreatedAt())
        .updatedAt(channel.getUpdatedAt())
        .type(channel.getType())
        .name(channel.getName()).build();
  }
}
