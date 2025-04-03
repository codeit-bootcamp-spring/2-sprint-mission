package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserResult;
import java.util.Optional;

public interface UpdateUserUseCase {

  UpdateUserResult update(UpdateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO);

}
