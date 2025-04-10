package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStatusCreateRequest request);

  UserStatus find(UUID userStatusKey);

  List<UserStatus> findAll();

  UserStatus update(UUID userStatusKey, UserStatusUpdateRequest request);

  UserStatus updateByUserKey(UUID userKey, UserStatusUpdateRequest request);

  void delete(UUID userStatusKey);

  void deleteByUserKey(UUID userKey);

}
