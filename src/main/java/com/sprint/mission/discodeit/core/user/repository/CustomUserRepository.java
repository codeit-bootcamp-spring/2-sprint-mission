package com.sprint.mission.discodeit.core.user.repository;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomUserRepository {

  List<User> findALlFromDB();

  Optional<User> findByUserId(UUID id);

  Optional<User> findByUserName(String name);
}
