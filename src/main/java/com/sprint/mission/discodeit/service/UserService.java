package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.User.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.User.UserDeleteDTO;
import com.sprint.mission.discodeit.DTO.User.UserFindDTO;
import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;

import java.util.List;
import java.util.UUID;


public interface UserService {

    void reset(boolean adminAuth);

    UUID registerUser(UserCreateDTO userCreateDTO);

    UserFindDTO findUser(String name);

    List<UserFindDTO> findUserAll();

    void printUser();

    boolean deleteUser(UserDeleteDTO userDeleteDTO);

    boolean updateUser(String userId, UserUpdateDTO userUpdateDTO);

}
