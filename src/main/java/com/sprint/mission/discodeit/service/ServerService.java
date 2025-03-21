package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServerService {

    void reset(boolean adminAuth);

//    Server create(ServerCreateRequestDTO serverCreateRequestDTO);

    User join(String serverId, String userId);

    User quit(String serverId, String userId);

    Server find(String serverId);

    List<Server> findServerAll(String ownerId);

//    Server update(String serverId, ServerCRUDDTO serverCRUDDTO);

    boolean delete(String serverId);

}
