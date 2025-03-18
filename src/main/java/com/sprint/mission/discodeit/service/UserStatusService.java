package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.usertstatus.UpdateUserStatusReqDto;
import com.sprint.mission.discodeit.dto.usertstatus.UserStatusResDto;
import com.sprint.mission.discodeit.dto.usertstatus.CreateUserStatusReqDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResDto create(CreateUserStatusReqDto createUserStatusReqDto);
    UserStatusResDto find(UUID userStatusId);
    List<UserStatusResDto> findAll();
    UserStatusResDto update(UUID userStatusId, UpdateUserStatusReqDto updateUserStatusReqDto);
    UserStatusResDto updateByUserId(UUID userId, UpdateUserStatusReqDto updateUserStatusReqDto);
    void delete(UUID userStatusId);
}
