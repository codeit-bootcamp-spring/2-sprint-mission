package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity._ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {

  _ReadStatus save(_ReadStatus readStatus);

  Optional<_ReadStatus> findById(UUID id);

  List<_ReadStatus> findAllByUserId(UUID userId);

  List<_ReadStatus> findAllBygetChannelId(UUID getChannelId);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteAllBygetChannelId(UUID getChannelId);
}
