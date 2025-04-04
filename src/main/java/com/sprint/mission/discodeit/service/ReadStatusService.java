package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatus save(ReadStatusRequest readStatusRequest);

  ReadStatus findById(UUID readStatusUUID);

  List<ReadStatus> findAllByUserId(UUID userId);

  ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest);

  void delete(UUID readStatusUUID);
}
