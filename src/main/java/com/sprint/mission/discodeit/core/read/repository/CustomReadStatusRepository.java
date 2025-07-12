package com.sprint.mission.discodeit.core.read.repository;

import com.sprint.mission.discodeit.core.read.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface CustomReadStatusRepository {

  ReadStatus findByReadStatusId(UUID readStatusId);

  List<UUID> findChannelIdByUserId(UUID userId);

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  List<ReadStatus> findAllByChannelIdIn(List<UUID> ids);
}
