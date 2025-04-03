package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {

  void save();

  void addReadStatus(ReadStatus readStatus);

  Optional<ReadStatus> findReadStatusById(UUID readStatusId);

  Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

  List<ReadStatus> findAllReadStatus();

  void deleteReadStatusById(UUID id);

  boolean existReadStatusById(UUID id);


}
