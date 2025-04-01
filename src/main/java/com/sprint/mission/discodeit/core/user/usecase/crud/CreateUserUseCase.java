package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserCreateRequestDTO;
import java.util.Optional;
import java.util.UUID;

public interface CreateUserUseCase {

  UUID create(UserCreateRequestDTO userCreateDTO,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO);

}
