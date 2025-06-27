package com.sprint.mission.discodeit.core.read.service;

import com.sprint.mission.discodeit.core.read.dto.ReadStatusDto;
import com.sprint.mission.discodeit.core.read.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.core.read.dto.request.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreateRequest request);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request);

  void delete(UUID readStatusId);

}
