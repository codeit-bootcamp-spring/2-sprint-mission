package com.sprint.mission.discodeit.adapter.inbound.message;

import com.sprint.mission.discodeit.adapter.inbound.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.message.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import java.util.UUID;

public final class MessageDtoMapper {

  private MessageDtoMapper() {
  }

  static CreateMessageCommand toCreateMessageCommand(MessageCreateRequest requestBody) {
    return new CreateMessageCommand(requestBody.authorId(), requestBody.channelId(),
        requestBody.content());
  }

  static UpdateMessageCommand toUpdateMessageCommand(UUID messageId,
      MessageUpdateRequest requestBody) {
    return new UpdateMessageCommand(messageId, requestBody.newText());
  }
}
