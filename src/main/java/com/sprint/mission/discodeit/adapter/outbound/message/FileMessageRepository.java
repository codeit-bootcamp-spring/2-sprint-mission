package com.sprint.mission.discodeit.adapter.outbound.message;

import static com.sprint.mission.discodeit.exception.message.MessageErrors.nullPointMessageIdError;

import com.sprint.mission.discodeit.adapter.outbound.FileRepositoryImpl;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import com.sprint.mission.discodeit.exception.SaveFileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileMessageRepository implements MessageRepositoryPort {

  private final FileRepositoryImpl<Map<UUID, Message>> fileRepository;
  private Map<UUID, Message> messageList = new ConcurrentHashMap<>();

  private final Path path = Paths.get(System.getProperty("user.dir"), "data", "MessageList.ser");

  public FileMessageRepository() {
    this.fileRepository = new FileRepositoryImpl<>(path);
    try {
      this.messageList = fileRepository.load();
    } catch (SaveFileNotFoundException e) {
      System.out.println("FileMessageRepository init");
    }
  }

  @Override
  public Message save(Message message) {
    messageList.put(message.getId(), message);
    fileRepository.save(messageList);
    return message;
  }

  @Override
  public Optional<Message> findById(UUID id) {
    return Optional.ofNullable(this.messageList.get(id));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return this.messageList.values().stream()
        .filter(message -> message.getChannelId().equals(channelId)).toList();
  }

  @Override
  public boolean existsById(UUID id) {
    if (id == null) {
      nullPointMessageIdError();
    }
    return this.messageList.containsKey(id);
  }


  @Override
  public void delete(UUID id) {
    if (id == null) {
      nullPointMessageIdError();
    }
    messageList.remove(id);
    fileRepository.save(messageList);
  }

}
