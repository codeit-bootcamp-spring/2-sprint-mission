package com.sprint.mission.discodeit.domain.readstatus.service;

import com.sprint.mission.discodeit.domain.readstatus.dto.ReadStatusResult;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusCreateRequest;

import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusUpdateRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusResult create(ReadStatusCreateRequest request);

  List<ReadStatusResult> getAllByUserId(UUID userId);

  ReadStatusResult updateLastReadTime(UUID readStatusId, ReadStatusUpdateRequest request);

  void delete(UUID readStatusId);

}
