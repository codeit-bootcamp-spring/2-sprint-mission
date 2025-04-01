package com.sprint.mission.discodeit.core.server.usecase;

import com.sprint.mission.discodeit.adapter.inbound.server.dto.ServerCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.server.dto.UpdateServerRequestDTO;
import com.sprint.mission.discodeit.core.server.entity.Server;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.core.server.port.ServerRepository;
import com.sprint.mission.discodeit.core.user.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BasicServerService implements ServerService {

  private final UserRepository userRepository;
  private final ServerRepository serverRepository;

  @Override
  public void reset(boolean adminAuth) {
    if (adminAuth) {
      serverRepository.reset();
    }
  }

  @CustomLogging
  @Override
  public Server create(ServerCreateRequestDTO serverCreateRequestDTO) {
    User owner = userRepository.findById(serverCreateRequestDTO.userId());
    Server server = new Server(owner.getId(), serverCreateRequestDTO.name());

    serverRepository.save(server, owner);

    return server;
  }

  @Override
  @CustomLogging
  public User join(UUID serverId, UUID userId) {
    Server server = serverRepository.findById(serverId);
    User user = userRepository.findById(userId);
    serverRepository.join(server, user);
    return user;
  }

  @Override
  public User quit(UUID serverId, UUID userId) {
    Server server = serverRepository.findById(serverId);
    User user = userRepository.findById(userId);
    serverRepository.quit(server, user);
    return user;
  }

  @Override
  public Server findById(UUID serverId) {
    return serverRepository.findById(serverId);

  }

  @Override
  public List<Server> findServerAll(UUID userId) {
    List<Server> serverList = serverRepository.findAllByUserId(userId);
    return serverList;

  }

  @Override
  public UUID update(UUID serverId, UUID userId, UpdateServerRequestDTO updateServerRequestDTO) {
    Server server = serverRepository.findById(serverId);
    Server update = serverRepository.update(server, updateServerRequestDTO);
    return update.getServerId();

  }

  @Override
  public void delete(UUID serverId, UUID userId) {
    serverRepository.remove(serverId);
  }
}
