package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity._Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFMessageRepository implements MessageRepository {

  private final Map<UUID, _Message> data;

  public JCFMessageRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public _Message save(_Message message) {
    this.data.put(message.getId(), message);
    return message;
  }

  @Override
  public Optional<_Message> findById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public List<_Message> findAllBygetChannelId(UUID getChannelId) {
    return this.data.values().stream()
        .filter(message -> message.getGetChannelId().equals(getChannelId))
        .toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    this.data.remove(id);
  }

  @Override
  public void deleteAllBygetChannelId(UUID getChannelId) {
    this.findAllBygetChannelId(getChannelId)
        .forEach(message -> this.deleteById(message.getId()));
  }
}
