package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusResult;
import java.util.List;
import java.util.UUID;

public interface FindReadStatusUseCase {

  ReadStatusResult find(UUID readStatusId);

  ReadStatusResult findReadStatusByUserId(UUID userId);

  List<ReadStatusResult> findAllByUserId(UUID userId);


}
