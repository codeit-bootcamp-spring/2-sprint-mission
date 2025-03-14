package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.UserRequest;
import com.sprint.mission.discodeit.service.dto.UserResponse;
import com.sprint.mission.discodeit.service.dto.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse create(UserRequest request);
    UserResponse find(UUID userId);
    List<UserResponse> findAll();
    UserResponse update(UserUpdateRequest request);
    void delete(UUID userId);
}
