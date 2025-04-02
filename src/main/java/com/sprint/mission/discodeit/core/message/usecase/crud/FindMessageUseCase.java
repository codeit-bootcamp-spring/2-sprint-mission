package com.sprint.mission.discodeit.core.message.usecase.crud;

import com.sprint.mission.discodeit.core.message.usecase.crud.dto.MessageResult;
import com.sprint.mission.discodeit.core.message.usecase.crud.dto.MessageListResult;
import java.util.UUID;

public interface FindMessageUseCase {

  MessageResult findMessageByMessageId(UUID messageId);

  MessageListResult findMessagesByChannelId(UUID channelId);

}
