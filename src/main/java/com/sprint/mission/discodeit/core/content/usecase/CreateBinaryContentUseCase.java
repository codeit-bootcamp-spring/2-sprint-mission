package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;

public interface CreateBinaryContentUseCase {

  BinaryContent create(CreateBinaryContentCommand createBinaryContentCommand);

}
