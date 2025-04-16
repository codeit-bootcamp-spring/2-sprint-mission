package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatus createReadStatus(CreateReadStatusRequest request);

  ReadStatus findReadStatusById(UUID readStatusId);

  ReadStatus findReadStatusByUserIdAndChannelId(UUID userId, UUID channelId);

  List<ReadStatus> findAll();

  ReadStatus updateReadStatus(UUID readStatusId, UpdateReadStatusRequest request);

  void deleteReadStatus(UUID readStatusId);

  public void validateReadStatusExists(UUID readStatusId);
}
