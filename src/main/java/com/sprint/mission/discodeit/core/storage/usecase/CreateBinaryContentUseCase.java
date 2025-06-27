package com.sprint.mission.discodeit.core.storage.usecase;

import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;

public interface CreateBinaryContentUseCase {

  BinaryContent create(BinaryContentCreateRequest binaryContentCreateRequest);

}
