package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(CreateUserRequest request);
    Optional<UserResponse> getUserById(UUID userId);
    List<UserResponse> getUsersByName(String name);
    List<UserResponse> getAllUsers();
    void updateUser(UpdateUserRequest request);
    void deleteUser(UUID userId);
}
