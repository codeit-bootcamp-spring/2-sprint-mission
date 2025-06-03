package com.sprint.mission.discodeit.core.status.repository;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserStatusRepository extends JpaRepository<UserStatus, UUID> {

  Optional<UserStatus> findByUser_Id(UUID userId);
}
