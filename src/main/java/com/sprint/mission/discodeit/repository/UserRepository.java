package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository {

  void save();

  void addUser(User user);

  Optional<User> findUserById(UUID userId);

  List<User> findUsersByIds(Set<UUID> userIds);

  List<User> findUserAll();

  Optional<User> findUserByName(String username);

  void deleteUserById(UUID userId);

  boolean existsById(UUID userId);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
