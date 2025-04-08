package com.sprint.mission.discodeit.core.message.usecase;

import java.util.UUID;

public interface DeleteMessageUseCase {

  void delete(UUID messageId);

}
