package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;

public interface CreateReadStatusUseCase {

  ReadStatus create(CreateReadStatusCommand command);


}
