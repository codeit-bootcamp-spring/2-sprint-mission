package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.DTO.UserCreateRequest;
import com.sprint.mission.discodeit.DTO.UserDto;
import com.sprint.mission.discodeit.DTO.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest);
    UserDto find(UUID userId);
    List<UserDto> findAll();
    User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest);
    void delete(UUID userId);
    boolean exists(UUID authorId);
}
