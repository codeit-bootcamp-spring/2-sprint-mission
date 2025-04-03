package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.user.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStatusCreateRequest request);

  UserStatus find(UUID userStatusId);

  UserStatus findByUserId(UUID userId);

  List<UserStatus> findAll();

  UserStatus update(UUID userStatusId, Instant newLastActiveAt);

  UserStatus updateByUserId(UUID userId);

  void delete(UUID userStatusId);

  void deleteByUserId(UUID userId);
}
