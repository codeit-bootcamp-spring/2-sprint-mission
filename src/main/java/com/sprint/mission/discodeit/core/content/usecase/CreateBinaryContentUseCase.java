package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;

public interface CreateBinaryContentUseCase {

  BinaryContent create(CreateBinaryContentCommand createBinaryContentCommand);

}
