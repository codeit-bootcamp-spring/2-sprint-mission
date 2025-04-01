package com.sprint.mission.discodeit.core.server.usecase;

import java.util.UUID;

public interface DeleteServerUseCase {

  void delete(UUID serverId, UUID userId);

}
