package com.sprint.mission.discodeit.core.user.service;

import com.sprint.mission.discodeit.core.status.usecase.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStatusCreateRequest command);

  void delete(UUID userStatusId);
}
