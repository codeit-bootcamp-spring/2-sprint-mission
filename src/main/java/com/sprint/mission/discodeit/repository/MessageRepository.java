package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity._Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

  _Message save(_Message message);

  Optional<_Message> findById(UUID id);

  List<_Message> findAllBygetChannelId(UUID getChannelId);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteAllBygetChannelId(UUID getChannelId);
}
