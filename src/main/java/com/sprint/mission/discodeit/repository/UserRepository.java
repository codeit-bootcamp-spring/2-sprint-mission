package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  @Query("SELECT u FROM User u "
      + "LEFT JOIN FETCH u.profile ")
  List<User> findAllWithProfile();

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE User u SET u.lastActiveAt = :lastActiveAt WHERE u.id = :userId")
  void updateLastActiveAt(@Param("userId") UUID userId, @Param("lastActiveAt") Instant lastActiveAt);
}
