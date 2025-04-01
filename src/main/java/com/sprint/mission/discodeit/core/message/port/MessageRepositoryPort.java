package com.sprint.mission.discodeit.core.message.port;

import com.sprint.mission.discodeit.core.message.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepositoryPort {

  Message save(Message message);

  Optional<Message> findById(UUID id);

  List<Message> findAllByChannelId(UUID channelId);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteAllByChannelId(UUID channelId);

  //    List<Message> findAllByMessageId(UUID messageId);
//
//    void remove(UUID messageId);
}
