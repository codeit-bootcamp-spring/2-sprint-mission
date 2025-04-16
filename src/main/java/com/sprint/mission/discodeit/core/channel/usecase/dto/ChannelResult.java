package com.sprint.mission.discodeit.core.channel.usecase.dto;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Channel Item")
public record ChannelResult(
    @Schema(description = "Channel Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,
    @Schema(description = "Channel Type", example = "PUBLIC")
    ChannelType type,
    @Schema(description = "Channel username", example = "string")
    String name,
    @Schema(description = "Channel newDescription", example = "string")
    String description,
    @Schema(
        description = "사용자 ID 목록",
        example = "[\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"]"
    )
    List<UserResult> participants,
    @Schema(description = "최근 메시지 시각", example = "2025-04-03T01:49:44.983Z")
    Instant lastMessageAt
) {

  public static ChannelResult create(Channel channel, List<UserResult> userIdList,
      Instant lastMessageAt) {
    return ChannelResult.builder()
        .id(channel.getId())
        .type(channel.getType())
        .name(channel.getName())
        .description(channel.getDescription())
        .participants(userIdList)
        .lastMessageAt(lastMessageAt)
        .build();
  }

}
