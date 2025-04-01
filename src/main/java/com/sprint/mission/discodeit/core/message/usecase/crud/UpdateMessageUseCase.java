package com.sprint.mission.discodeit.core.message.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.message.dto.UpdateMessageDTO;
import java.util.UUID;

public interface UpdateMessageUseCase {

  UUID update(UUID messageId, UpdateMessageDTO updateMessageDTO);

}
