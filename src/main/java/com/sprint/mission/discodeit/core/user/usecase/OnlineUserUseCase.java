package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.status.usecase.user.dto.OnlineUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusResult;

public interface OnlineUserUseCase {

  UserStatusResult online(OnlineUserStatusCommand command);

}
