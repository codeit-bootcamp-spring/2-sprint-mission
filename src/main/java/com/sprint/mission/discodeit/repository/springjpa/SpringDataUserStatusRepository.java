package com.sprint.mission.discodeit.repository.springjpa;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface SpringDataUserStatusRepository extends JpaRepository<UserStatus, UUID> {
    Optional<UserStatus> findByUser_Id(UUID userId);
    void deleteByUser_Id(UUID userId);
}
