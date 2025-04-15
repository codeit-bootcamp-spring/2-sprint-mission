package com.sprint.mission.discodeit.core.status.usecase.user;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface FindUserStatusUseCase {

  UserStatus findByUserId(UUID userId);

  UserStatus findByStatusId(UUID userStatusId);

  List<UserStatus> findAll();


}
