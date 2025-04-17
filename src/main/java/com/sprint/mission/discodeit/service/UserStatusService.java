package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.userStatus.CreateUserStatusCommand;
import com.sprint.mission.discodeit.dto.service.userStatus.CreateUserStatusResult;
import com.sprint.mission.discodeit.dto.service.userStatus.FindUserStatusResult;


import com.sprint.mission.discodeit.dto.service.userStatus.UpdateUserStatusResult;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  CreateUserStatusResult create(CreateUserStatusCommand createUserStatusCommand);

  FindUserStatusResult findById(UUID id);

  FindUserStatusResult findByUserId(UUID userId);

  List<FindUserStatusResult> findAll();

  UpdateUserStatusResult updateByUserId(UUID id);

  void delete(UUID id);

  void deleteByUserId(UUID userId);
}
