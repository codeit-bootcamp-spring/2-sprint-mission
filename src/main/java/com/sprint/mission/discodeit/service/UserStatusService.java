package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatus createUserStatus(UserStatusCreateRequest request);

  UserStatus findById(UUID userStatusId);

  List<UserStatus> findAll();

  UserStatus findByUserId(UUID userId);

  void updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request);

  UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

  void deleteUserStatus(UUID userStatusId);

}
