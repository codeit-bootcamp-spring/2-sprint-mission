package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageDto;
import java.util.List;

public interface CreateMessageUseCase {

  MessageDto create(CreateMessageCommand command,
      List<CreateBinaryContentCommand> binaryContentCommands);

}
