package com.sprint.mission.discodeit.core.message.controller;

import com.sprint.mission.discodeit.core.message.controller.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.core.message.controller.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import java.util.UUID;

public final class MessageDtoMapper {

  private MessageDtoMapper() {
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
