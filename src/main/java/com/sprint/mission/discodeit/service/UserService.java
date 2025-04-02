package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.*;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserCreateResponse create(UserCreateRequest requestDto);
    UserResponse find(UUID userId);
    List<UserResponse> findAll();
    UserUpdateResponse update(UserUpdateRequest requestDto);
    void delete(UUID userId);
}
