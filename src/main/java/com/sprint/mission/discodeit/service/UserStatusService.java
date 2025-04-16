package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.dto.userstatusdto.*;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusResponseDto create(UserStatusCreateDto userStatusCreateDto);
    UserStatusResponseDto find(UserStatusFindDto userStatusFindDto);
    List<UserStatusResponseDto> findAll();
    UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateDto userStatusUpdateRequest);
    void delete(UserStatusDeleteDto userStatusDeleteDto);
}
