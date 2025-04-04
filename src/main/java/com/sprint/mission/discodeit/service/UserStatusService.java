package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    void create(UserStatusDto userCreateDto);
    UserStatusDto find(UUID id);
    List<UserStatusDto> findAll();
    void update(UserStatusDto userStatusDto);
    void updateByUserId(UUID userid);
    void delete(UUID id);
}
