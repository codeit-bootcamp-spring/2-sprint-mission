package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
    name = "discordit.repository.type",
    havingValue = "jcf")
public class JCFMessageRepository implements MessageRepository {

  private static final Map<UUID, Message> messages = new HashMap<>();

  @Override
  public void save() {
  }

  @Override
  public void addMessage(Message message) {
    messages.put(message.getId(), message);
  }

  @Override
  public Optional<Message> findMessageById(UUID messageId) {
    return Optional.ofNullable(messages.get(messageId));
  }

  @Override
  public Optional<Message> findLatestMessageByChannelId(UUID channelId) {
    return messages.values().stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .max(Comparator.comparing(Message::getCreatedAt));
  }

  @Override
  public List<Message> findMessageAll() {
    return new ArrayList<>(messages.values());
  }

  @Override
  public void deleteMessageById(UUID messageId) {
    messages.remove(messageId);
  }

  @Override
  public boolean existsById(UUID messageId) {
    return messages.containsKey(messageId);
  }

  @Override
  public void deleteMessageByChannelId(UUID channelId) {
    messages.values().removeIf(message -> message.getChannelId().equals(channelId));
  }
}
