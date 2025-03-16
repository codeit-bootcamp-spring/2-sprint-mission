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

    UUID create(ServerCreateDTO serverCreateDTO);

    UUID join(ServerJoinDTO serverJoinDTO);

    Server find(String serverId);

    List<Server> findServerAll(String ownerId);

    void print(String userId);

    boolean delete(ServerDeleteDTO serverDeleteDTO);

    boolean update(String serverId, ServerUpdateDTO serverUpdateDTO);

}
