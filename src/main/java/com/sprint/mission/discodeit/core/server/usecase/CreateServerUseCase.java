package com.sprint.mission.discodeit.core.server.usecase;

import com.sprint.mission.discodeit.adapter.inbound.server.dto.ServerCreateRequestDTO;
import com.sprint.mission.discodeit.core.server.entity.Server;

public interface CreateServerUseCase {

  Server create(ServerCreateRequestDTO serverCreateRequestDTO);

}
