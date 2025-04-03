package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

  Message save(Message message);

  Optional<Message> findByMessageId(UUID id);

  List<Message> findAll();

  List<Message> findByChannelId(UUID channelId);

  void delete(UUID id);

  Optional<Instant> findLastMessageCreatedAtByChannelId(UUID channelId);
}
