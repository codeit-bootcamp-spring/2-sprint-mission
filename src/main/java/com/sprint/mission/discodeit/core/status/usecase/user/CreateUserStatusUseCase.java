package com.sprint.mission.discodeit.core.status.usecase.user;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.CreateUserStatusCommand;

public interface CreateUserStatusUseCase {

  UserStatus create(CreateUserStatusCommand command);

}
