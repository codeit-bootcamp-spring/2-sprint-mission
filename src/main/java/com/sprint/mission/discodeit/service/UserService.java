package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import org.springframework.web.multipart.MultipartFile;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateWithFileRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateWithFileRequest;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserDto create(UserCreateWithFileRequest request);

    UserDto find(UUID userId);

    UserDto update(UUID userId, UserUpdateWithFileRequest request);

    void delete(UUID userId);

    List<UserDto> findAll();

    boolean isUserOnline(User user);

}
