package com.sprint.mission.discodeit.core.message.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.core.message.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreateMessageUseCase {

  Message create(UUID userId, UUID channelId, MessageCreateRequestDTO messageWriteDTO,
      List<Optional<BinaryContentCreateRequestDTO>> binaryContentDTOs);

}
