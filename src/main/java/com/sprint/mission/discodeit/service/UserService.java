package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.User.*;

import java.util.List;
import java.util.UUID;


public interface UserService {

    void reset(boolean adminAuth);

    UUID register(UserCRUDDTO userCRUDDTO);

    UserFindDTO find(String userId);

    List<UserFindDTO> findAll();

    void print();

    boolean delete(UserCRUDDTO userCRUDDTO);

    boolean update(String userId, UserCRUDDTO userCRUDDTO);

}
