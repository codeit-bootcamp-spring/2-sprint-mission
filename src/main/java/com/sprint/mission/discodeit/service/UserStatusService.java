package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.SaveUserStatusParamDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusParamDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    void save(SaveUserStatusParamDto saveUserStatusParamDto);
    UserStatus findById(UUID userStatusUUID);
    List<UserStatus> findAll();
    void update(UpdateUserStatusParamDto updateUserStatusParamDto);
    void updateByUserId(UUID userUUID);
    void delete(UUID userStatusUUID);
}
