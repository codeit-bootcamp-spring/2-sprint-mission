package com.sprint.mission.discodeit.core.user.repository;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.List;
import java.util.UUID;

public interface CustomUserRepository {

  List<User> findAllFromDB();

  User findByUserId(UUID id);

  User findByUserName(String name);
}
