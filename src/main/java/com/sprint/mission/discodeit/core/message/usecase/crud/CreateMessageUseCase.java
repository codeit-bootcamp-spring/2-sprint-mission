package com.sprint.mission.discodeit.core.message.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.core.message.usecase.crud.dto.MessageCreateCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import java.util.List;
import java.util.Optional;

public interface CreateMessageUseCase {

  Message create(MessageCreateCommand command,
      List<Optional<BinaryContentCreateRequestDTO>> binaryContentDTOs);

}
