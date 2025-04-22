package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusResult;

public interface CreateReadStatusUseCase {

  ReadStatusResult create(CreateReadStatusCommand command);


}
