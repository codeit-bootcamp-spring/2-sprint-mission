package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.DTO.legacy.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServerRepository {
    void reset();

    Server save(Server server, User user);

    User join(Server server, User user);

    User quit(Server server, User user);

    Server find(UUID serverId);

    Server findByOwnerId(UUID userId);

    List<Server> findAllByUserId(UUID userId);

    Server update(Server targetServer, ServerCRUDDTO serverCRUDDTO);

    void remove(UUID userId);
}
