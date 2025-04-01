package com.sprint.mission.discodeit.core.server.usecase;

import com.sprint.mission.discodeit.core.server.entity.Server;
import java.util.List;
import java.util.UUID;

public interface FindServerUseCase {

  Server findById(UUID serverId);

  List<Server> findServerAll(UUID userId);

}
