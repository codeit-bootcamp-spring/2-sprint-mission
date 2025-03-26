package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequestDTO dto);
    Optional<UserStatus> find(UUID id);
    List<UserStatus> findAll();
    UserStatus update(UserStatusUpdateRequestDTO dto);

    UserStatus updateByUserId(UUID userId, UserStatusUpdateRequestDTO dto);

    void delete(UUID id);

    Optional<UserStatus> findByUserId(UUID userId);
}