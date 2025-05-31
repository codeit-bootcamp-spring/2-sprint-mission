package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.storage.usecase.dto.BinaryContentCreateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserCreateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import java.util.Optional;

public interface CreateUserUseCase {

  UserDto create(UserCreateCommand command,
      Optional<BinaryContentCreateCommand> binaryContentDTO);

}
