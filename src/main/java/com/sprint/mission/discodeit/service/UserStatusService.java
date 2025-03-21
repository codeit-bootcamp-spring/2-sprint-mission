package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateUserStatusRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.UpdateUserStatusRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(CreateUserStatusRequest request);
    Optional<UserStatus> getById(UUID userId);
    List<UserStatus> getAll();
    void update(UpdateUserStatusRequest request);
    void updateByUserId(UUID userId);
    void delete(UUID userId);
}
