package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UUID userId);

  UserStatusDto findById(UUID UserStatusId);

  UserStatusDto findByUserId(UUID UserId);

  List<UserStatusDto> findAll();


  UserStatusDto update(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID UserStatusId);
}
