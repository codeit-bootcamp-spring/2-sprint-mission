package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.userStatus.CreatedUserStatusParam;
import com.sprint.mission.discodeit.dto.service.userStatus.UpdateUserStatusParam;
import com.sprint.mission.discodeit.dto.service.userStatus.UserStatusDTO;

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
