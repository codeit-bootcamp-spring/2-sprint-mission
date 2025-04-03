package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.controller.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.controller.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity._ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  _ReadStatus create(ReadStatusCreateRequest request);

  _ReadStatus find(UUID readStatusId);

  List<_ReadStatus> findAllByUserId(UUID userId);

  _ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request);

  void delete(UUID readStatusId);
}
