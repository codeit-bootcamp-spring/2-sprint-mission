package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentCreateCommand;

public interface CreateBinaryContentUseCase {

  BinaryContent create(BinaryContentCreateCommand binaryContentCreateCommand);

}
