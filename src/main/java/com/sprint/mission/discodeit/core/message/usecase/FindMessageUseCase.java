package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.message.usecase.dto.MessageResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageListResult;
import java.util.UUID;

public interface FindMessageUseCase {

  MessageResult findMessageByMessageId(UUID messageId);

  MessageListResult findMessagesByChannelId(UUID channelId);

}
