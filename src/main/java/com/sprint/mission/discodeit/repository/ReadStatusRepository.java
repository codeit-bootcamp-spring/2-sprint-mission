package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {

  ReadStatus save(ReadStatus readStatus);

  Optional<ReadStatus> find(UUID readStatusId);

  Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId);

  List<ReadStatus> findByChannelId(UUID channelId);

  List<ReadStatus> findByUserId(UUID userId);

  void delete(UUID readStatusId);
}
