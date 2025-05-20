package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentCreateCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageCreateCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageDto;
import java.util.List;

public interface CreateMessageUseCase {

  MessageDto create(MessageCreateCommand command,
      List<BinaryContentCreateCommand> binaryContentCommands);

}
