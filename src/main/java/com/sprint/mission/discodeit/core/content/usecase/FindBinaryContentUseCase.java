package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentResult;
import java.util.List;
import java.util.UUID;

public interface FindBinaryContentUseCase {

  BinaryContentResult findById(UUID binaryId);

  List<BinaryContentResult> findAllByIdIn(List<UUID> binaryContentIds);


}
