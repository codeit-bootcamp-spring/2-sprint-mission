package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import java.util.List;
import java.util.UUID;

public interface FindUserUseCase {

  UserResult findById(UUID userId);

  boolean existsById(UUID userId);

  List<UserResult> findAll();

}
