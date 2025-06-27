package com.sprint.mission.discodeit.core.user.repository;

import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaUserStatusRepository extends JpaRepository<UserStatus, UUID> {

  @Query("SELECT us from UserStatus us where us.user.id = :userId")
  Optional<UserStatus> findByUserId(@Param("userId") UUID userId);

}
