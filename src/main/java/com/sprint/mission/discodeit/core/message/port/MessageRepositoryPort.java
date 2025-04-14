package com.sprint.mission.discodeit.core.message.port;

import com.sprint.mission.discodeit.core.message.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepositoryPort {

  Message save(Message message);

  Optional<Message> findById(UUID id);

  List<Message> findAllByChannelId(UUID channelId);

  boolean existsById(UUID id);

  void delete(UUID id);

}
