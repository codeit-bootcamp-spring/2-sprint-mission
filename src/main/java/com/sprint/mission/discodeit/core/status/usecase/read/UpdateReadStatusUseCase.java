package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusResult;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;

public interface UpdateReadStatusUseCase {

  ReadStatusResult update(UpdateReadStatusCommand command);


}
