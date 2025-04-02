package com.sprint.mission.discodeit.core.status.port;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {

  ReadStatus save(ReadStatus readStatus);

  Optional<ReadStatus> findById(UUID readStatusId);

  ReadStatus findByUserId(UUID userId);

  ReadStatus findByChannelId(UUID channelId);

  ReadStatus findByUserAndChannelId(UUID userId, UUID channelId);

  List<ReadStatus> findAllByUserId(UUID userID);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  void delete(UUID readStatusId);
}
