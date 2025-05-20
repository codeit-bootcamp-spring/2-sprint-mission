package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserDto create(UserCreateRequest userCreateRequest,
        MultipartFile profileImageFile);

    UserDto find(UUID userId);
    

    UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        MultipartFile profileImageFile);

    void delete(UUID userId);

    List<UserDto> findAll();
}
