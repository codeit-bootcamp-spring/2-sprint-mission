package com.sprint.mission.discodeit.domain.user.repository;

import com.sprint.mission.discodeit.domain.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByName(String name);

  Optional<User> findByEmail(String email);

  boolean existsUserByName(String name);

  boolean existsUserByEmail(String email);

  Optional<User> findByBinaryContentId(UUID binaryContentId);

}
