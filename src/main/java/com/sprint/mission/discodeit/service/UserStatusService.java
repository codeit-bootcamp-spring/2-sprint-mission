package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(CreateUserStatusDto dto);
    UserStatus find(UUID userStatusKey);
    List<UserStatus> findAll();
    UserStatus update(UpdateUserStatusDto dto);
    void delete(UUID userStatusKey);
    void deleteByUserKey(UUID userKey);

}
