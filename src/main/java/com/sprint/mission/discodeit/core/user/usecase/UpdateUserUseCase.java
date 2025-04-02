package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import java.util.Optional;

public interface UpdateUserUseCase {

  void update(UpdateUserCommand command,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO);

}
