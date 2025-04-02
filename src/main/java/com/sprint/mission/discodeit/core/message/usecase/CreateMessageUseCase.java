package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import java.util.List;

public interface CreateMessageUseCase {

  Message create(CreateMessageCommand command,
      List<CreateBinaryContentCommand> binaryContentCommands);

}
