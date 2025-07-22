package com.sprint.mission.discodeit.core.user.repository;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, UUID>, CustomUserRepository {

  boolean existsByNameOrEmail(String name, String email);

  boolean existsByEmail(String email);

  boolean existsByName(String name);
}
