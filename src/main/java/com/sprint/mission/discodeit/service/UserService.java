package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.User.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.User.UserDeleteDTO;
import com.sprint.mission.discodeit.DTO.User.UserFindDTO;
import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;

import java.util.List;
import java.util.UUID;


public interface UserService {

    void reset(boolean adminAuth);

    UUID register(UserCreateDTO userCreateDTO);

    UserFindDTO find(String name);

    List<UserFindDTO> findAll();

    void print();

    boolean delete(UserDeleteDTO userDeleteDTO);

    boolean update(String userId, UserUpdateDTO userUpdateDTO);

}
