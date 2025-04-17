package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  @EntityGraph(attributePaths = "user")
  List<UserStatus> findAllBy();

  @EntityGraph(attributePaths = "user")
  Optional<UserStatus> findById(UUID userStatusId);

  @EntityGraph(attributePaths = "user")
  Optional<UserStatus> findByUserId(UUID userId);

  @EntityGraph(attributePaths = "user")
  void deleteByUserId(UUID userId);

  @EntityGraph(attributePaths = "user")
  boolean existsByUserId(UUID userId);
}
