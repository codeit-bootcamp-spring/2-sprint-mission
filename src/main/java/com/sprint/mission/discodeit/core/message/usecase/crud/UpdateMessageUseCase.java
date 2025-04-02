package com.sprint.mission.discodeit.core.message.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.message.dto.UpdateMessageCommand;

public interface UpdateMessageUseCase {

  void update(UpdateMessageCommand command);

}
