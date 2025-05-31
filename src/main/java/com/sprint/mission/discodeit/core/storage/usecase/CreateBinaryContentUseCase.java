package com.sprint.mission.discodeit.core.storage.usecase;

import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.usecase.dto.BinaryContentCreateCommand;

public interface CreateBinaryContentUseCase {

  BinaryContent create(BinaryContentCreateCommand binaryContentCreateCommand);

}
