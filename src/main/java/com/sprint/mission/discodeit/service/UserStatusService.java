package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.StatusDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    boolean createUserStatus(UUID statusDto);
    StatusDto.StatusResponse updateUserStatus(UUID userId, StatusDto.StatusRequest statusDto);
    List<StatusDto.Summary> findAllUsers();
    StatusDto.Summary findById(UUID userId);
    boolean deleteUserStatus(UUID userId);
    void setUserOnline(UUID userId);
}
