package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.message.usecase.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;

public interface UpdateMessageUseCase {

  MessageDto update(UpdateMessageCommand command);

}
