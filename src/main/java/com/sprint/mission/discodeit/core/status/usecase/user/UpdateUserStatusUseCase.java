package com.sprint.mission.discodeit.core.status.usecase.user;

import com.sprint.mission.discodeit.core.status.usecase.user.dto.UpdateUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusResult;

public interface UpdateUserStatusUseCase {

  UserStatusResult update(UpdateUserStatusCommand command);


}
