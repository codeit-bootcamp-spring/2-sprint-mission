package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import org.springframework.web.multipart.MultipartFile;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserDto create(UserCreateRequest userCreateRequest, MultipartFile profileImageFile);

    UserDto find(UUID userId);

    UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        Optional<MultipartFile> profileRequest);


    UserDto updateRole(RoleUpdateRequest roleUpdateRequest);

    void delete(UUID userId);

    List<UserDto> findAll();

    boolean isUserOnline(User user);

}
