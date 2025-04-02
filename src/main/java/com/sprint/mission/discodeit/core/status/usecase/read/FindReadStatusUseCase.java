package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface FindReadStatusUseCase {

  ReadStatus findReadStatusById(UUID readStatusId);

  ReadStatus findReadStatusByUserId(UUID userId);

  List<ReadStatus> findAllByUserId(UUID userId);


}
