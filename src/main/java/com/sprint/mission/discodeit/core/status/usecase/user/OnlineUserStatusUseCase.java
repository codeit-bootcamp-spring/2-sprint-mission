package com.sprint.mission.discodeit.core.status.usecase.user;

import java.util.UUID;

public interface OnlineUserStatusUseCase {

  boolean isOnline(UUID userId);

}
