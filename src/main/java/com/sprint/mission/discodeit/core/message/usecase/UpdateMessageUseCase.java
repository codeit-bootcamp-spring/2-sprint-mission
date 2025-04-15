package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.message.usecase.dto.MessageResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;

public interface UpdateMessageUseCase {

  MessageResult update(UpdateMessageCommand command);

}
