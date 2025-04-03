package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity._User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  _User save(_User user);

  Optional<_User> findById(UUID id);

  Optional<_User> findByUsername(String username);

  List<_User> findAll();

  boolean existsById(UUID id);

  void deleteById(UUID id);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
