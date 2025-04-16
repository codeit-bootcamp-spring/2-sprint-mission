package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto createReadStatus(CreateReadStatusRequest request);

  ReadStatusDto findReadStatusById(UUID readStatusId);

  ReadStatusDto findReadStatusByUserIdAndChannelId(UUID userId, UUID channelId);

  List<ReadStatusDto> findAll();

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto updateReadStatus(UUID readStatusId, UpdateReadStatusRequest request);

  void deleteReadStatus(UUID readStatusId);

  void validateReadStatusExists(UUID readStatusId);
}
