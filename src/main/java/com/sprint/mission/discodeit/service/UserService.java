package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.UserService.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserService.UserFindDto;
import com.sprint.mission.discodeit.dto.UserService.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest userCreateRequest, BinaryContentDTO binaryContentDTO);
    User find(UUID userId);
    UserFindDto findWithStatus(UUID id);
    List<User> findAll();
    List<UserFindDto>  findAllWithStatus();
    User update(UserUpdateRequest userUpdateRequest);
    void delete(UUID userId);
}
