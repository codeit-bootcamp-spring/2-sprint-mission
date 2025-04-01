package com.sprint.mission.discodeit.core.server.port;

import com.sprint.mission.discodeit.core.server.entity.Server;
import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepositoryPort {

  Server save(Server server, User user);

  User join(Server server, User user);

  User quit(Server server, User user);

  Server findById(UUID serverId);

  Server findByOwnerId(UUID userId);

  List<Server> findAllByUserId(UUID userId);

  void remove(UUID userId);
}
