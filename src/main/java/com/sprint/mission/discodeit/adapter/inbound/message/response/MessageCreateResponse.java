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
//    boolean success,
//    String message
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<UUID> attachmentIds
) {

  public static MessageCreateResponse create(Message message) {
    return MessageCreateResponse.builder()
        .id(message.getMessageId())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .content(message.getText())
        .channelId(message.getChannelId())
        .authorId(message.getUserId())
        .build();
  }
}
