package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.CreateUserCommand;
import java.util.Optional;

public interface CreateUserUseCase {

  void create(CreateUserCommand command,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO);

}
