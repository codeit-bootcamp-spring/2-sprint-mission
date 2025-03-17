package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.User.*;

import java.util.List;
import java.util.UUID;


public interface UserService {

    void reset(boolean adminAuth);

    UUID register(UserDTO userDTO);

    UserFindDTO find(String name);

    List<UserFindDTO> findAll();

    void print();

    boolean delete(UserDTO userDTO);

    boolean update(String userId, UserDTO userDTO);

}
