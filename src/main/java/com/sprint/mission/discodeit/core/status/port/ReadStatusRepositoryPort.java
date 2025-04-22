package com.sprint.mission.discodeit.core.status.port;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepositoryPort {

  ReadStatus save(ReadStatus readStatus);

  Optional<ReadStatus> findById(UUID readStatusId);

  ReadStatus findByUserId(UUID userId);

  ReadStatus findByChannelId(UUID channelId);

  Instant findLastActiveAtByChannelId(UUID channelId);

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  boolean existsId(UUID readStatusId);

  void delete(UUID readStatusId);
}
