package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.discodeit.util.FileUtils.loadAndSave;
import static com.sprint.mission.discodeit.util.FileUtils.loadObjectsFromFile;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {

  private final Path messagePath;

  public FileMessageRepository(
      @Value("${discodeit.repository.file-directory.message-path}") Path messagePath) {
    this.messagePath = messagePath;
  }

  @Override
  public Message save(Message message) {
    loadAndSave(messagePath, (Map<UUID, Message> messages) ->
        messages.put(message.getId(), message)
    );

    return message;
  }

  @Override
  public Optional<Message> findByMessageId(UUID id) {
    Map<UUID, Message> messages = loadObjectsFromFile(messagePath);

    return Optional.ofNullable(messages.get(id));
  }

  @Override
  public List<Message> findAll() {
    Map<UUID, Message> messages = loadObjectsFromFile(messagePath);

    return messages.values()
        .stream()
        .toList();
  }

  @Override
  public List<Message> findByChannelId(UUID channelId) {
    Map<UUID, Message> messages = loadObjectsFromFile(messagePath);

    return messages.values()
        .stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public void delete(UUID id) {
    loadAndSave(messagePath, (Map<UUID, Message> messages) ->
        messages.remove(id)
    );
  }

  @Override
  public Optional<Instant> findLastMessageCreatedAtByChannelId(UUID channelId) {
    Map<UUID, Message> messages = loadObjectsFromFile(messagePath);

    return messages.values()
        .stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .max(Comparator.comparing(Message::getCreatedAt))
        .map(Message::getCreatedAt);
  }
}
