package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.userStatus.CreatedUserStatusParam;
import com.sprint.mission.discodeit.dto.service.userStatus.UpdateUserStatusParam;
import com.sprint.mission.discodeit.dto.service.userStatus.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatus userStatus);
    UserStatus findById(UUID id);
    List<UserStatus> findAll();
    UUID update(UpdateUserStatusParam updateUserStatusParam);
    UUID updateByUserId(UUID userId, UpdateUserStatusParam updateUserStatusParam);
    void delete(UUID id);
}
