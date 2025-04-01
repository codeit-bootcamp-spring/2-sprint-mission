package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequestDto dto);

    UserStatus findById(UUID userStatusId);

    List<UserStatus> findAll();

    UserStatus update(UserStatusUpdateRequestDto userStatusUpdate);

    UserStatus updateByUserId(UUID userId, Instant newActivatedAt);

    void delete(UUID userStatusId);
}