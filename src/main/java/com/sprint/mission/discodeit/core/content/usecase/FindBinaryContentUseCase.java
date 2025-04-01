package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface FindBinaryContentUseCase {

  BinaryContent findById(UUID binaryId);

  List<BinaryContent> findAllByIdIn();


}
