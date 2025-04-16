package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

    @NonNull
    UserStatus save(@NonNull UserStatus userStatus);

    @NonNull
    Optional<UserStatus> findById(UUID id);

    Optional<UserStatus> findByUserId(UUID userId);

    @NonNull
    List<UserStatus> findAll();

    boolean existsById(@NonNull UUID id);

    void deleteById(@NonNull UUID id);

    void deleteByUserId(UUID userId);
}
