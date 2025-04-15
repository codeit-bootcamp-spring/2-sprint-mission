package com.sprint.mission.discodeit.adapter.inbound.channel.response;

import com.sprint.mission.discodeit.adapter.inbound.user.response.UserResponse;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Public Channel 성공적으로 생성됨")
public record ChannelResponse(
    @Schema(description = "Channel Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,
    @Schema(description = "Channel Type", example = "PUBLIC")
    ChannelType type,
    @Schema(description = "Channel username", example = "string")
    String name,
    @Schema(description = "Channel newDescription", example = "string")
    String description,

    List<UserResponse> participants,

    @Schema(description = "최근 메시지 시각", example = "2025-04-03T01:49:44.983Z")
    Instant lastMessageAt
) {

}
