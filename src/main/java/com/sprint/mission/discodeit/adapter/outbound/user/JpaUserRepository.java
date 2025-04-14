package com.sprint.mission.discodeit.adapter.outbound.user;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByName(String name);

  Optional<User> findByEmail(String email);
}
