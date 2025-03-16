package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.Server.ServerUpdateDTO;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ServerRepository {
    void reset();

    UUID save(User user, Server server);

    UUID join(User user, Server server);

    UUID quit(User user, Server server);

    Server find(UUID serverId);

    List<Server> findAllByUserId(UUID userId);

    UUID update(Server targetServer, ServerUpdateDTO serverUpdateDTO);

    void remove(User owner, Server server);
}
