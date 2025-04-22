package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  @Query("select u from User u left join fetch u.profile left join fetch u.status")
  List<User> findAllWithProfileAndStatus();

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
