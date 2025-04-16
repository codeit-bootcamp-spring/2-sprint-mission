package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto findById(UUID UserStatusId);

  UserStatusDto findByUserId(UUID UserId);

  List<UserStatusDto> findAll();

  UserStatus create(UUID userId);

  UserStatus update(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID UserStatusId);
}
