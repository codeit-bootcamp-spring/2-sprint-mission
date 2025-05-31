package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.storage.usecase.dto.BinaryContentCreateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserUpdateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import java.util.Optional;

public interface UpdateUserUseCase {

  UserDto update(UserUpdateCommand command,
      Optional<BinaryContentCreateCommand> binaryContentDTO);

}
