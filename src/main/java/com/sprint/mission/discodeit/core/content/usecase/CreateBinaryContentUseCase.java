package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentResult;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;

public interface CreateBinaryContentUseCase {

  BinaryContentResult create(CreateBinaryContentCommand createBinaryContentCommand);

}
