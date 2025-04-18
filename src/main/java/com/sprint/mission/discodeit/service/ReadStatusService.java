package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreateRequest request);

  ReadStatus find(UUID id);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  List<UUID> findAllChannelIdByUserId(UUID userId);

  ReadStatusDto update(UUID id, ReadStatusUpdateRequest request);

  void delete(UUID id);
}