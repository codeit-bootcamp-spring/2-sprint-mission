package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.UserStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.DTO.UserStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.DTO.UserStatus.UserStatusDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusDto create(CreateUserStatusDto dto);
    UserStatusDto find(UUID id);
    List<UserStatusDto> findAll();
    UserStatusDto update(UpdateUserStatusDto dto);
    void delete(UUID id);
}
