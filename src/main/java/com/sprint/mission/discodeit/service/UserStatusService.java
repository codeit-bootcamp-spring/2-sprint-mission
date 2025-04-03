package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusFindDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatus create(UserStatusCreateDto userStatusCreateDto);
    UserStatus getUser(UserStatusFindDto userStatusFindDto);
    List<UserStatus> getAllUser();
    UserStatus updateByUserId(UUID userId);
    void delete(UserStatusDeleteDto userStatusDeleteDto);
}
