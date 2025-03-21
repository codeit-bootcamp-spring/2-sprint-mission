package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.CreateServerRequestDTO;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ServerService {

    void reset(boolean adminAuth);

    Server create(CreateServerRequestDTO createServerRequestDTO);

    User join(UUID serverId, UUID userId);

    User quit(UUID serverId, UUID userId);

    Server findById(UUID serverId);

    List<Server> findServerAll(UUID ownerId);

//    Server update(String serverId, ServerCRUDDTO serverCRUDDTO);

    void delete(UUID serverId);

}
