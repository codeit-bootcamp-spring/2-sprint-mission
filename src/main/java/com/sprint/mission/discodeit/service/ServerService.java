package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.create.ServerCreateRequestDTO;
import com.sprint.mission.discodeit.dto.update.UpdateServerRequestDTO;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
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
