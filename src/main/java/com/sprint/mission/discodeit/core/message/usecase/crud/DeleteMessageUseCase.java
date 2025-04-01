package com.sprint.mission.discodeit.core.message.usecase.crud;

import java.util.UUID;

public interface DeleteMessageUseCase {

  void delete(UUID messageId);

}
