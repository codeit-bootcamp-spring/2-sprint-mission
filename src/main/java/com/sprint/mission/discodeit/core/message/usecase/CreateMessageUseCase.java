package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageCreateCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageDto;
import java.util.List;

public interface CreateMessageUseCase {

  MessageDto create(MessageCreateCommand command,
      List<BinaryContentCreateRequest> binaryContentCommands);

}
