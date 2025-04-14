package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
    name = "discordit.repository.type",
    havingValue = "file",
    matchIfMissing = true)
public class FileMessageRepository implements MessageRepository {

  private static final String fileName = "messages.dat";
  private static Map<UUID, Message> messages = new ConcurrentHashMap<>();
  private final FileStorageManager fileStorageManager;

  @Autowired
  public FileMessageRepository(FileStorageManager fileStorageManager) {
    this.fileStorageManager = fileStorageManager;
    messages = fileStorageManager.loadFile(fileName);
  }

  @Override
  public void save() {
    fileStorageManager.saveFile(fileName, messages);
  }

  @Override
  public void addMessage(Message message) {
    messages.put(message.getId(), message);
    save();
  }

  @Override
  public Optional<Message> findMessageById(UUID messageId) {
    return Optional.ofNullable(messages.get(messageId));
  }

  @Override
  public Optional<Message> findLatestMessageByChannelId(UUID channelId) {
    return messages.values().stream()
        .filter(message -> message.getChannel().getId().equals(channelId))
        .max(Comparator.comparing(Message::getCreatedAt));
  }

  @Override
  public List<Message> findMessageAll() {
    return new ArrayList<>(messages.values());
  }

  @Override
  public void deleteMessageById(UUID messageId) {
    messages.remove(messageId);
    save();
  }

  @Override
  public void deleteMessageByChannelId(UUID channelId) {
    messages.values().removeIf(message -> message.getChannel().getId().equals(channelId));
    save();
  }

  @Override
  public boolean existsById(UUID messageId) {
    return messages.containsKey(messageId);
  }
}
