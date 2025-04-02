package com.sprint.mission.discodeit.core.status.usecase.user;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UpdateUserStatusCommand;

public interface UpdateUserStatusUseCase {

  UserStatus update(UpdateUserStatusCommand command);


}
