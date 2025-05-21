package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto create(UserCreateRequest userCreateRequest, BinaryContentCreateRequest binaryContentCreateRequest);

    UserDto findById(UUID userId);

    List<UserDto> findAll();

    UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
                   BinaryContentCreateRequest binaryContentCreateRequest);

    void delete(UUID userId);
}
