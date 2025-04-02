package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UpdateUserCommand;
import java.util.Optional;
import java.util.UUID;

public interface UpdateUserUseCase {

  void update(UUID userId, UpdateUserCommand command,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO);

}
