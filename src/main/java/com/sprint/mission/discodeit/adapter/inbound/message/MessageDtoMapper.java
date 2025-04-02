package com.sprint.mission.discodeit.adapter.inbound.message;

import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import java.util.UUID;

public final class MessageDtoMapper {

  private MessageDtoMapper() {
  }

  static CreateMessageCommand toCreateMessageCommand(UUID userId, UUID channelId,
      MessageCreateRequest requestBody) {
    return new CreateMessageCommand(userId, channelId, requestBody.text());
  }

  static UpdateMessageCommand toUpdateMessageCommand(UUID messageId,
      MessageUpdateRequest requestBody) {
    return new UpdateMessageCommand(messageId, requestBody.newText());
  }
}
