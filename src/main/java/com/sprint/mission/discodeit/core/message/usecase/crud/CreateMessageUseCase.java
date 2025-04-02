package com.sprint.mission.discodeit.core.message.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.core.message.usecase.crud.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import java.util.List;
import java.util.Optional;

public interface CreateMessageUseCase {

  Message create(CreateMessageCommand command,
      List<Optional<BinaryContentCreateRequestDTO>> binaryContentDTOs);

}
