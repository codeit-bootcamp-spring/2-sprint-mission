package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.StatusDto;
import com.sprint.mission.discodeit.dto.UserDto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    StatusDto.Summary createUserStatus(StatusDto.Create statusDto);
    UserDto.Summary updateByUserId(@NotBlank UUID userId);
    StatusDto.Summary update(StatusDto.Summary statusDto);
    List<StatusDto.Summary> findAllUsers();
    StatusDto.Summary findById(UUID userId);
    StatusDto.ResponseDelete deleteUserStatus(UUID userId);
    void deleteByUserId(UUID userId);
}
