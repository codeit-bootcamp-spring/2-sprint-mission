package com.sprint.mission.discodeit.core.message.port;

import com.sprint.mission.discodeit.core.message.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageRepositoryPort {

  Message save(Message message);

  Optional<Message> findById(UUID id);

  Slice<Message> findById(UUID id, Pageable pageable);

  List<Message> findAllByChannelId(UUID channelId);

  Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  Slice<Message> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable);

  boolean existsById(UUID id);

  void delete(UUID id);

}
