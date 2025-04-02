package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import java.util.Optional;

public interface CreateUserUseCase {

  void create(CreateUserCommand command,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO);

}
