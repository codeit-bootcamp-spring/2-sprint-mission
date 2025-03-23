package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.UserService.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.UserService.UserFindDTO;
import com.sprint.mission.discodeit.DTO.UserService.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateDTO userCreateDTO, BinaryContentDTO binaryContentDTO);
    User find(UUID userId);
    UserFindDTO findWithStatus(UUID id);
    List<User> findAll();
    List<UserFindDTO>  findAllWithStatus();
    User update(UserUpdateDTO userUpdateDTO);
    void delete(UUID userId);
}
