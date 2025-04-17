package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDto;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusDto create(UserStatusCreateDto userStatusCreateDto);

    UserStatusDto findById(UUID userStatusId);

    List<UserStatusDto> findAll();

    UserStatusDto update(UUID userStatusId, UserStatusUpdateDto userStatusUpdateDto);

    UserStatusDto updateByUserId(UUID userId, UserStatusUpdateByUserIdDto userStatusUpdateByUserIdDto);

    void delete(UUID userStatusId);
}
