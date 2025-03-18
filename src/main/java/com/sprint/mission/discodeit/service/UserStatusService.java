package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.UserStatus.CreatedUserStatusParam;
import com.sprint.mission.discodeit.dto.service.UserStatus.UpdateUserStatusParam;
import com.sprint.mission.discodeit.dto.service.UserStatus.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusDTO create(CreatedUserStatusParam createdUserStatusParam);
    UserStatusDTO findById(UUID id);
    List<UserStatusDTO> findAll();
    UUID update(UpdateUserStatusParam updateUserStatusParam);
    UUID updateByUserId(UUID userId, UpdateUserStatusParam updateUserStatusParam);
    void delete(UUID id);
}
