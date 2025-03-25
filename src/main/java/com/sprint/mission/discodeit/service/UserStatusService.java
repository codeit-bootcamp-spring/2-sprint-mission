package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.UserStatusInfoDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusInfoDto findById(UUID id);

    List<UserStatusInfoDto> findAll();

    void create(CreateUserStatusDto dto);

    void update(UpdateUserStatusDto dto);

    void delete(UUID id);
}
