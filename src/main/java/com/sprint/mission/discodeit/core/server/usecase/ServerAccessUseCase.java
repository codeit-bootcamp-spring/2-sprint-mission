package com.sprint.mission.discodeit.core.server.usecase;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.UUID;

public interface ServerAccessUseCase {

  User join(UUID serverId, UUID userId);

  User quit(UUID serverId, UUID userId);

}
