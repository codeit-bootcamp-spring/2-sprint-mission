package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.status.usecase.user.dto.OnlineUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusDto;

public interface OnlineUserUseCase {

  UserStatusDto online(OnlineUserStatusCommand command);

}
