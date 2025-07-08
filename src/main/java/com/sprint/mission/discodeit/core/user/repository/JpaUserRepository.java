package com.sprint.mission.discodeit.core.user.repository;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

  @Query("SELECT u FROM User u "
      + "LEFT JOIN FETCH u.profile ")
  List<User> findAllWithProfileAndStatus();

  Optional<User> findByName(String name);

  boolean existsByNameOrEmail(String name, String email);
}
