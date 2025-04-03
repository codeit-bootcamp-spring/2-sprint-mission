package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserDeleteRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto create(UserCreateRequest useRequest);
    UserDto read(UUID userKey);
    List<UserDto> readAll();
    User update(UserUpdateRequest userRequest);
    void delete(UserDeleteRequest userDeleteRequest);
}
