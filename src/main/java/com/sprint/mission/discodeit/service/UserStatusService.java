package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusResponse;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import java.util.UUID;

public interface UserStatusService {

  UpdateUserStatusResponse updateByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest);
}
