package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.usecase.dto.ReadStatusDto;
import com.sprint.mission.discodeit.core.status.usecase.dto.ReadStatusUpdateCommand;

public interface UpdateReadStatusUseCase {

  ReadStatusDto update(ReadStatusUpdateCommand command);


}
