package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.file.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserDto createUser(CreateUserRequest request, Optional<CreateBinaryContentRequest> profileOpt);

    Optional<UserDto> getUserById(UUID userId);

    List<UserDto> getUsersByName(String name);

    List<UserDto> getAllUsers();

    void updateUser(UpdateUserRequest request, Optional<CreateBinaryContentRequest> profileOpt);

    void deleteUser(UUID userId);
}
