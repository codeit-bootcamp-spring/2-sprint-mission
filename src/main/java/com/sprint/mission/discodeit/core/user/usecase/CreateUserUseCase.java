package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserResult;
import java.util.Optional;

public interface CreateUserUseCase {

  CreateUserResult create(CreateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO);

}
