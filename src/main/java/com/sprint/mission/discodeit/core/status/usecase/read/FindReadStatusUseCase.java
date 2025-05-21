package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.usecase.dto.ReadStatusDto;
import java.util.List;
import java.util.UUID;

public interface FindReadStatusUseCase {

  ReadStatusDto findByReadStatusId(UUID readStatusId);

  ReadStatusDto findByUserId(UUID userId);

  List<ReadStatusDto> findAllByUserId(UUID userId);


}
