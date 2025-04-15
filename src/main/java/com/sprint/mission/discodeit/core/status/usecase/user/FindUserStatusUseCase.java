package com.sprint.mission.discodeit.core.status.usecase.user;

import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusResult;
import java.util.List;
import java.util.UUID;

public interface FindUserStatusUseCase {

  UserStatusResult findByUserId(UUID userId);

  UserStatusResult findByStatusId(UUID userStatusId);

  List<UserStatusResult> findAll();


}
