package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.common.ReadStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusResponse create(ReadStatusCreateRequest request);

  ReadStatusResponse find(UUID readStatusId);

  ReadStatusResponse update(UUID readStatusId, Instant newLastReadAt);

  List<ReadStatusResponse> findAllByUserId(UUID userId);

  void delete(UUID readStatusId);
}
