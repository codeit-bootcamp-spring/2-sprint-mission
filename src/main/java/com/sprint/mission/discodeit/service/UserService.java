package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserDto create(UserCreateRequest userCreateRequest, MultipartFile profileImageFile);

    UserDto find(UUID userId);


    UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        MultipartFile profileImageFile);

    void delete(UUID userId);

    List<UserDto> findAll();
}
