package com.sprint.mission.discodeit.adapter.inbound.message.response;

import com.sprint.mission.discodeit.core.message.entity.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Message 성공적으로 생성됨")
public record MessageCreateResponse(
    @Schema(description = "Message Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,
    @Schema(description = "Message 생성 시각", example = "2025-04-03T01:49:44.983Z")
    Instant createdAt,
    @Schema(description = "Message 수정 시각", example = "2025-04-03T01:49:44.983Z")
    Instant updatedAt,
    @Schema(description = "Message content", example = "string")
    String content,
    @Schema(description = "Channel Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID channelId,
    @Schema(description = "User Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID authorId,
    @Schema(
        description = "Binary Content ID 목록",
        example = "[\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"]"
    )
    List<UUID> attachmentIds
) {

  public static MessageCreateResponse create(Message message) {
    return MessageCreateResponse.builder()
        .id(message.getId())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .content(message.getContent())
        .channelId(message.getChannelId())
        .authorId(message.getUserId())
        .attachmentIds(message.getAttachmentIds())
        .build();
  }

}
