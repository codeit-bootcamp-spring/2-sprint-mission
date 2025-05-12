package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusDto;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;

public interface UpdateReadStatusUseCase {

  ReadStatusDto update(UpdateReadStatusCommand command);


}
