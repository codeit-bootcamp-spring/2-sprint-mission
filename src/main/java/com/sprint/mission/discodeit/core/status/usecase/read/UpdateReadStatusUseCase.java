package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusDto;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusUpdateCommand;

public interface UpdateReadStatusUseCase {

  ReadStatusDto update(ReadStatusUpdateCommand command);


}
