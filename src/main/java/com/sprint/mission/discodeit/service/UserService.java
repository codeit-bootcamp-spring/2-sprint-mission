package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.binarycontent.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto create(CreateUserRequest request);

    UserDto create(CreateUserRequest request, CreateBinaryContentRequest binaryContentRequest);

    UserDto findByUserId(UUID userId);

    List<UserDto> findAll();

    UserDto update(UUID userId, UpdateUserRequest request);

    UserDto update(UUID userId, UpdateUserRequest request,
        CreateBinaryContentRequest binaryContentRequest);

    void remove(UUID userId);
}
