package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.status.usecase.dto.UserStatusDto;
import com.sprint.mission.discodeit.core.user.controller.dto.UserStatusRequest;
import java.util.UUID;

public interface OnlineUserUseCase {

  UserStatusDto online(UUID userId, UserStatusRequest requestBody);

}
