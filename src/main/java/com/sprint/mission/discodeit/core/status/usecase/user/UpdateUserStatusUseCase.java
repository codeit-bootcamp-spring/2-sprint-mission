package com.sprint.mission.discodeit.core.status.usecase.user;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.usecase.dto.UserStatusUpdateCommand;

public interface UpdateUserStatusUseCase {

  UserStatus update(UserStatusUpdateCommand command);


}
