package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.SaveUserStatusParamDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  void save(SaveUserStatusParamDto saveUserStatusParamDto);

  UserStatus findById(UUID userStatusUUID);

  List<UserStatus> findAll();

  UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);

  void delete(UUID userStatusUUID);
}
