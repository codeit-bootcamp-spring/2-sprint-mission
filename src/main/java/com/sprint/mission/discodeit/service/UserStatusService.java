package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.request.userstatusdto.UserStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userstatusdto.UserStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.request.userstatusdto.UserStatusFindDto;
import com.sprint.mission.discodeit.service.dto.request.userstatusdto.UserStatusUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.UserStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusResponseDto create(UserStatusCreateDto userStatusCreateDto);
    UserStatusResponseDto find(UserStatusFindDto userStatusFindDto);
    List<UserStatusResponseDto> findAll();
    UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateDto userStatusUpdateRequest);
    void delete(UserStatusDeleteDto userStatusDeleteDto);
}
