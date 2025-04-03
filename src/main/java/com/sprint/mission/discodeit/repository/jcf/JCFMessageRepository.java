package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {

  private final Map<UUID, Message> messages = new HashMap<>();

  @Override
  public Message save(Message message) {
    messages.put(message.getId(), message);

    return message;
  }

  @Override
  public Optional<Message> findByMessageId(UUID id) {
    return Optional.ofNullable(messages.get(id));
  }

  @Override
  public List<Message> findAll() {
    return messages.values()
        .stream()
        .toList();
  }

  @Override
  public List<Message> findByChannelId(UUID channelId) {
    return messages.values()
        .stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public Optional<Instant> findLastMessageCreatedAtByChannelId(UUID channelId) {
    return messages.values()
        .stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .max(Comparator.comparing(Message::getCreatedAt))
        .map(Message::getCreatedAt);
  }

  @Override
  public void delete(UUID id) {
    messages.remove(id);
  }
}
