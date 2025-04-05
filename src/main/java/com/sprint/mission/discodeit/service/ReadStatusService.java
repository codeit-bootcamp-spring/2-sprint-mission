package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatus createReadStatus(ReadStatusCreateRequest request);

  ReadStatus findById(UUID readStatusId);

  List<ReadStatus> findAllByUserId(UUID userId);

  ReadStatus updateReadStatus(UUID readStatusId, ReadStatusUpdateRequest request);

  void deleteReadStatus(UUID readStatusId);

}
