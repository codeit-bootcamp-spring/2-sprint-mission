package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusDto create(CreateUserStatusRequest request);

    UserStatusDto findByUserStatusId(UUID id);

    List<UserStatusDto> findAll();

    UserStatusDto update(UUID userStatusId, UpdateUserStatusRequest request);

    UserStatusDto updateByUserId(UUID userId, UpdateUserStatusRequest request);

    void delete(UUID userId);
}
