package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageResult;
import java.util.List;

public interface CreateMessageUseCase {

  CreateMessageResult create(CreateMessageCommand command,
      List<CreateBinaryContentCommand> binaryContentCommands);

}
