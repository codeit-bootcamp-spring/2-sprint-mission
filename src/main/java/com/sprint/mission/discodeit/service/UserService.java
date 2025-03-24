package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreate request);
    UserResponse find(UUID userId);
    List<UserResponse> findAll();
    User update(UserUpdate dto);
    void delete(UUID userId);
}
