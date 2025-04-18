package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {

  UserStatus save(UserStatus userStatus);

  UserStatus update(UUID userStatusId, UserStatusUpdateRequest request);

  List<UserStatus> findAll();

  UserStatus findById(UUID userStatusId);

  UserStatus findByUserId(UUID userId);

  void delete(UUID userStatusId);
}
