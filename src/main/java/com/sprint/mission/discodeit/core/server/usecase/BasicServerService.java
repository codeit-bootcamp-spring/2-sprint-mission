package com.sprint.mission.discodeit.core.server.usecase;

import com.sprint.mission.discodeit.adapter.inbound.server.dto.ServerCreateRequestDTO;
import com.sprint.mission.discodeit.core.server.entity.Server;
import com.sprint.mission.discodeit.core.server.port.ServerRepositoryPort;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BasicServerService implements ServerService {

  private final UserRepositoryPort userRepositoryPort;
  private final ServerRepositoryPort serverRepositoryPort;

  @CustomLogging
  @Override
  public Server create(ServerCreateRequestDTO serverCreateRequestDTO) {
    User owner = userRepositoryPort.findById(serverCreateRequestDTO.userId());
    Server server = Server.create(owner.getId(), serverCreateRequestDTO.name());

    serverRepositoryPort.save(server, owner);

    return server;
  }

  @Override
  @CustomLogging
  public User join(UUID serverId, UUID userId) {
    Server server = serverRepositoryPort.findById(serverId);
    User user = userRepositoryPort.findById(userId);
    serverRepositoryPort.join(server, user);
    return user;
  }

  @Override
  public User quit(UUID serverId, UUID userId) {
    Server server = serverRepositoryPort.findById(serverId);
    User user = userRepositoryPort.findById(userId);
    serverRepositoryPort.quit(server, user);
    return user;
  }

  @Override
  public Server findById(UUID serverId) {
    return serverRepositoryPort.findById(serverId);

  }

  @Override
  public List<Server> findServerAll(UUID userId) {
    List<Server> serverList = serverRepositoryPort.findAllByUserId(userId);
    return serverList;

  }

//  @Override
//  public UUID update(UUID serverId, UUID userId, UpdateServerRequestDTO updateServerRequestDTO) {
//    Server server = serverRepositoryPort.findById(serverId);
//    Server update = serverRepositoryPort.update(server, updateServerRequestDTO);
//    return update.getServerId();
//
//  }

  @Override
  public void delete(UUID serverId, UUID userId) {
    serverRepositoryPort.remove(serverId);
  }
}
