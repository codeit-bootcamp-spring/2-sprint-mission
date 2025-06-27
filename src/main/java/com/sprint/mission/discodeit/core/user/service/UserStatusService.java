package com.sprint.mission.discodeit.core.user.service;

import com.sprint.mission.discodeit.core.user.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStatusCreateRequest command);

  void delete(UUID userId);
}
