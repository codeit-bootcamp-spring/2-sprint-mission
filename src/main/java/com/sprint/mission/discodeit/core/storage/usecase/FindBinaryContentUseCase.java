package com.sprint.mission.discodeit.core.storage.usecase;

import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface FindBinaryContentUseCase {

  BinaryContent findById(UUID binaryId);

  List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);


}
