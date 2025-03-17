package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Server.*;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.UUID;

public interface ServerService {

    void reset(boolean adminAuth);

    UUID create(ServerDTO serverDTO);

    UUID join(ServerDTO serverDTO);

    Server find(String serverId);

    List<Server> findServerAll(String ownerId);

    void print(String userId);

    boolean delete(ServerDTO serverDTO);

    boolean update(String serverId, ServerDTO serverDTO);

}
