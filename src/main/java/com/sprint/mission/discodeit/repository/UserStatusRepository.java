package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    List<UserStatus> findAll();
    Optional<UserStatus> findById(UUID userStatusId);
    boolean existsById(UUID userStatusId);
    void deleteById(UUID userStatusId);

    Optional<UserStatus> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
