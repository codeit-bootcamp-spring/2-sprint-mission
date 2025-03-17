package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Server.*;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.UUID;

public interface ServerService {

    void reset(boolean adminAuth);

    UUID create(ServerCRUDDTO serverCRUDDTO);

    UUID join(ServerCRUDDTO serverCRUDDTO);

    Server find(String serverId);

    List<Server> findServerAll(String ownerId);

    void print(String userId);

    boolean delete(ServerCRUDDTO serverCRUDDTO);

    boolean update(String serverId, ServerCRUDDTO serverCRUDDTO);

}
