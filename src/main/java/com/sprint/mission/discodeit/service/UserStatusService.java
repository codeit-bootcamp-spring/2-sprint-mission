package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusInfoDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusInfoDto findById(UUID id);

    List<UserStatusInfoDto> findAll();

    void create(UUID userId);

    void update(UUID userId);

    void delete(UUID id);
}
