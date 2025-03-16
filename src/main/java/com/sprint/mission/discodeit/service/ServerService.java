package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Server.ServerCreateDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerDeleteDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerJoinDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerUpdateDTO;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.UUID;

public interface ServerService {

    void reset(boolean adminAuth);

    UUID createServer(ServerCreateDTO serverCreateDTO);

    UUID joinServer(ServerJoinDTO serverJoinDTO);

    Server findServer(String ownerId, String name);

    List<Server> findServerAll(String ownerId);

    void printServer(String userId);

    boolean deleteServer(ServerDeleteDTO serverDeleteDTO);

    boolean updateServer(ServerDeleteDTO serverDeleteDTO, ServerUpdateDTO serverUpdateDTO);

}
