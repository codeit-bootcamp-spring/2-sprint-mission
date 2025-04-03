package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.controller.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity._UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  _UserStatus create(UserStatusCreateRequest request);

  _UserStatus find(UUID userStatusId);

  List<_UserStatus> findAll();

  _UserStatus update(UUID userStatusId, UserStatusUpdateRequest request);

  _UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID userStatusId);
}
