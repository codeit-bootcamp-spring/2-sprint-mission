package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.usecase.dto.ReadStatusCreateCommand;
import com.sprint.mission.discodeit.core.status.usecase.dto.ReadStatusDto;

public interface CreateReadStatusUseCase {

  ReadStatusDto create(ReadStatusCreateCommand command);


}
