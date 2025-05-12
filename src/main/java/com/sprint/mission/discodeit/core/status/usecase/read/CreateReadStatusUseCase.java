package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusDto;

public interface CreateReadStatusUseCase {

  ReadStatusDto create(CreateReadStatusCommand command);


}
