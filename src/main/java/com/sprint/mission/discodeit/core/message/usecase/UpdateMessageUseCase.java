package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageResult;

public interface UpdateMessageUseCase {

  UpdateMessageResult update(UpdateMessageCommand command);

}
