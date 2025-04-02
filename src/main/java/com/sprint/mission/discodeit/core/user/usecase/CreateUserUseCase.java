package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import java.util.Optional;

public interface CreateUserUseCase {

  void create(CreateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO);

}
