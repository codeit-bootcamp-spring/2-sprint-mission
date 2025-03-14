package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Server.ServerCreateDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerDeleteDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerJoinDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerUpdateDTO;
import com.sprint.mission.discodeit.DTO.User.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.User.UserDeleteDTO;
import com.sprint.mission.discodeit.DTO.User.UserFindDTO;
import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;


public interface UserService {

    void reset(boolean adminAuth);

    UUID registerUser(UserCreateDTO userCreateDTO);

    UUID createServer(ServerCreateDTO serverCreateDTO);

    UUID joinServer(ServerJoinDTO serverJoinDTO);

    UserFindDTO findUser(String name);

    List<UserFindDTO> findUserAll();

    Server findServer(String ownerId, String name);

    List<Server> findServerAll(String ownerId);

    void printUser();

    void printServer(String userId);

    boolean deleteUser(UserDeleteDTO userDeleteDTO);

    boolean deleteServer(ServerDeleteDTO serverDeleteDTO);

    boolean updateUser(String userId, UserUpdateDTO userUpdateDTO);


    boolean updateServer(ServerDeleteDTO serverDeleteDTO, ServerUpdateDTO serverUpdateDTO);
}
