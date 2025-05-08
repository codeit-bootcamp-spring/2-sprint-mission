package com.sprint.mission.discodeit.core.user.repository;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

  @Override
  @EntityGraph(attributePaths = {"profile", "userStatus"})
  List<User> findAll();

  Optional<User> findByName(String name);

  Optional<User> findByEmail(String email);
}
