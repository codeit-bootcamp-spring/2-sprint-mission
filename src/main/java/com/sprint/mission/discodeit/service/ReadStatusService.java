package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto createReadStatus(ReadStatusCreateRequest request);

  ReadStatusDto findById(UUID readStatusId);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto updateReadStatus(UUID readStatusId, ReadStatusUpdateRequest request);

  void deleteReadStatus(UUID readStatusId);

}
