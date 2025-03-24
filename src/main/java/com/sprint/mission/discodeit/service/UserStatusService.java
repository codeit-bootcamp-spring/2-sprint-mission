package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusDto userStatusDto);
    UserStatus findById(UUID userStatusId);
    List<UserStatus> findAll();
    UserStatus update(UUID userStatusId, UserStatusDto userStatusDto);
    UserStatus updateByUserId(UUID userId, UserStatusDto userStatusDto);
    void delete(UUID userStatusId);
}
