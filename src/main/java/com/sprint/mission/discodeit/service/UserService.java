package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDto create(UserCreateRequest userCreateRequest,
        Optional<BinaryContentCreateRequest> profileCreateRequest);

    UserDto find(UUID userId);

    Page<UserDto> findAll(Pageable pageable);

    UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        Optional<BinaryContentCreateRequest> profileCreateRequest);

    void delete(UUID userId);
}
