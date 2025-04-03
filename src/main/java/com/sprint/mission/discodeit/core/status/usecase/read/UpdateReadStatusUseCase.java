package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;

public interface UpdateReadStatusUseCase {

  ReadStatus updateReadStatus(UpdateReadStatusCommand command);


}
