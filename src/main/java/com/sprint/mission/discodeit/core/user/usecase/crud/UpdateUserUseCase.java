package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UpdateUserRequestDTO;
import java.util.Optional;
import java.util.UUID;

public interface UpdateUserUseCase {

  UUID update(UUID userId, UpdateUserRequestDTO updateUserRequestDTO,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO);

}
