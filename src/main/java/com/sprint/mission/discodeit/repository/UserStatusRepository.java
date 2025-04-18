package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository {

  UserStatus save(UserStatus userStatus);

  Optional<UserStatus> findById(UUID id);

  Optional<UserStatus> findByUser(User user);

  List<UserStatus> findAll();

  void delete(UserStatus userStatus);

  void deleteByUser(User user);
}
