package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Optional;
import java.util.UUID;
import java.util.*;

public interface UserStatusRepository {

  void saveData();

  void register(UserStatus userStatus);

  Optional<UserStatus> findById(UUID id);

  Optional<UserStatus> findByUserId(UUID userId);

  List<UserStatus> findAll();

  boolean update(UserStatus userStatus);

  boolean delete(UUID id);

}
