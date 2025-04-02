package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UserListResult;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UserResult;
import java.util.UUID;

public interface FindUserUseCase {

  UserResult findById(UUID userId);

  boolean existsById(UUID userId);

  UserListResult findAll();

}
