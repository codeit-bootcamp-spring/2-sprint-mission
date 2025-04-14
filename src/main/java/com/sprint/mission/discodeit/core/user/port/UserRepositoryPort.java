package com.sprint.mission.discodeit.core.user.port;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepositoryPort {

  User save(User user);

  Optional<User> findById(UUID id);

  Optional<User> findByName(String name);

  Optional<User> findByEmail(String email);

  List<User> findAll();

  void delete(UUID id);

  boolean existId(UUID id);
}