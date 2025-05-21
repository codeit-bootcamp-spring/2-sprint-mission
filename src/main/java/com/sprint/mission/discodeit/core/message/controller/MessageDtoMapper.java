package com.sprint.mission.discodeit.core.message.controller;

import com.sprint.mission.discodeit.core.message.controller.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.core.message.controller.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageCreateCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageUpdateCommand;
import java.util.UUID;

public final class MessageDtoMapper {

  private MessageDtoMapper() {
  }


  public static MessageCreateCommand toCreateMessageCommand(MessageCreateRequest requestBody) {
    return new MessageCreateCommand(requestBody.authorId(), requestBody.channelId(),
        requestBody.content());
  }

  public static MessageUpdateCommand toUpdateMessageCommand(UUID messageId,
      MessageUpdateRequest requestBody) {
    return new MessageUpdateCommand(messageId, requestBody.newText());
  }
}
