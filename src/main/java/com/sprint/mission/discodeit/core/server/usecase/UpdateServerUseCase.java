package com.sprint.mission.discodeit.core.server.usecase;

import com.sprint.mission.discodeit.adapter.inbound.server.dto.UpdateServerRequestDTO;
import java.util.UUID;

public interface UpdateServerUseCase {

  UUID update(UUID serverId, UUID userId, UpdateServerRequestDTO updateServerRequestDTO);

}
