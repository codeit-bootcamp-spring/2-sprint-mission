package com.sprint.mission.discodeit.core.user.service.usecase;

import com.sprint.mission.discodeit.core.user.dto.response.UserStatusDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserStatusRequest;
import java.util.UUID;

public interface OnlineUserUseCase {

  UserStatusDto online(UUID userId, UserStatusRequest request);

}
