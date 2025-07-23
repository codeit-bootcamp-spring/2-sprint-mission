package com.sprint.mission.discodeit.domain.read.service;

import com.sprint.mission.discodeit.domain.read.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.domain.read.dto.ReadStatusDto;
import com.sprint.mission.discodeit.domain.read.dto.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreateRequest request);

  ReadStatusDto find(UUID readStatusId);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request);

  void delete(UUID readStatusId);
}
