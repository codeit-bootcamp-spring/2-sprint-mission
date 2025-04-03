package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity._UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

  _UserStatus save(_UserStatus userStatus);

  Optional<_UserStatus> findById(UUID id);

  Optional<_UserStatus> findByUserId(UUID userId);

  List<_UserStatus> findAll();

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteByUserId(UUID userId);
}
