package com.sprint.mission.discodeit.adapter.outbound.message;

import static com.sprint.mission.discodeit.exception.message.MessageErrors.nullPointMessageIdError;

import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFMessageRepository implements MessageRepositoryPort {

  private final Map<UUID, Message> messageList = new ConcurrentHashMap<>();

  @Override
  public Message save(Message message) {
    messageList.put(message.getId(), message);
    return message;
  }

  @Override
  public Optional<Message> findById(UUID id) {
    return Optional.ofNullable(messageList.get(id));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageList.values().stream()
        .filter(message -> message.getChannelId().equals(channelId)).toList();
  }

  @Override
  public boolean existsById(UUID id) {
    if (id == null) {
      nullPointMessageIdError();
    }
    return messageList.containsKey(id);
  }

  @Override
  public void delete(UUID id) {
    if (id == null) {
      nullPointMessageIdError();
    }
    messageList.remove(id);
  }

}
