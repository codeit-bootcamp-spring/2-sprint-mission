package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  @EntityGraph(attributePaths = {"status", "profile"})
  @Query("select u from User u where u.id = :id")
  Optional<User> findWithDetailsById(@Param("id") UUID id);

  @EntityGraph(attributePaths = {"status", "profile"})
  @Query("select u from User u")
  List<User> findAllWithDetails();

  Optional<User> findByUsername(String username);

  @EntityGraph(attributePaths = {"status", "profile"})
  List<User> findByIdIn(Set<UUID> userIds);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
