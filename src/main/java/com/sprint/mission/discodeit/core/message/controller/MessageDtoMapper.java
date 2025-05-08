package com.sprint.mission.discodeit.core.message.controller;

import com.sprint.mission.discodeit.core.content.controller.BinaryContentDtoMapper;
import com.sprint.mission.discodeit.core.message.controller.request.MessageCreateRequest;
import com.sprint.mission.discodeit.core.message.controller.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.message.controller.response.MessageResponse;
import com.sprint.mission.discodeit.core.user.controller.UserDtoMapper;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import java.util.UUID;

public final class MessageDtoMapper {

  private MessageDtoMapper() {
  }

  public static MessageResponse toCreateResponse(MessageResult result) {
    return new MessageResponse(result.id(), result.createdAt(), result.updatedAt(),
        result.content(), result.channelId(),
        UserDtoMapper.toCreateResponse(result.author()), result.attachments().stream()
        .map(BinaryContentDtoMapper::toCreateResponse)
        .toList());
  }

  public static CreateMessageCommand toCreateMessageCommand(MessageCreateRequest requestBody) {
    return new CreateMessageCommand(requestBody.authorId(), requestBody.channelId(),
        requestBody.content());
  }

  public static UpdateMessageCommand toUpdateMessageCommand(UUID messageId,
      MessageUpdateRequest requestBody) {
    return new UpdateMessageCommand(messageId, requestBody.newText());
  }
}
