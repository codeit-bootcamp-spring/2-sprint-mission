package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  public void createReadStatus(CreateReadStatusRequest request);

  ReadStatus findReadStatusById(UUID readStatusId);

  ReadStatus findReadStatusByUserIdAndChannelId(UUID userId, UUID channelId);

  List<ReadStatus> findAll();

  void updateReadStatus(UUID readStatusId);

  void deleteReadStatus(UUID id);
}
