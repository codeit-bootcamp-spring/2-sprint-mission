package com.sprint.mission.discodeit.core.server.usecase;

import com.sprint.mission.discodeit.adapter.inbound.server.dto.ServerCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.server.dto.UpdateServerRequestDTO;
import com.sprint.mission.discodeit.core.server.entity.Server;
import com.sprint.mission.discodeit.core.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ServerService {

  void reset(boolean adminAuth);

  Server create(ServerCreateRequestDTO serverCreateRequestDTO);

  User join(UUID serverId, UUID userId);

  User quit(UUID serverId, UUID userId);

  Server findById(UUID serverId);

  List<Server> findServerAll(UUID userId);

  UUID update(UUID serverId, UUID userId, UpdateServerRequestDTO updateServerRequestDTO);

  void delete(UUID serverId, UUID userId);

}
