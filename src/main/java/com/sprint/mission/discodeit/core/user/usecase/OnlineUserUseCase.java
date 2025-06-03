package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.status.usecase.dto.UserStatusOnlineCommand;
import com.sprint.mission.discodeit.core.status.usecase.dto.UserStatusDto;

public interface OnlineUserUseCase {

  UserStatusDto online(UserStatusOnlineCommand command);

}
