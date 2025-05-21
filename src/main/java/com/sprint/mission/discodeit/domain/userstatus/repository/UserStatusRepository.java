package com.sprint.mission.discodeit.domain.userstatus.repository;

import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {
    Optional<UserStatus> findByUser_Id(UUID userId);
}
