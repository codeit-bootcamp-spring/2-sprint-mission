package com.sprint.mission.discodeit.adapter.outbound.message;

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

  //    private final FileRepositoryImpl<Map<UUID, List<Message>>> fileRepository;
//    private Map<UUID, List<Message>> messageList = new ConcurrentHashMap<>();
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
    messageList.put(message.getMessageId(), message);
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
    return this.messageList.containsKey(id);
  }


  @Override
  public void deleteById(UUID id) {
    this.messageList.remove(id);
    fileRepository.save(messageList);
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    this.findAllByChannelId(channelId)
        .forEach(message -> this.deleteById(message.getMessageId()));
    fileRepository.save(messageList);
  }

//    @Override
//    public Message save(Channel channel, Message message) {
//        List<Message> messages = messageList.getOrDefault(channel.getChannelId(), new ArrayList<>());
//        messages.add(message);
//        messageList.put(channel.getChannelId(), messages);
//        fileRepository.save(messageList);
//        return message;
//    }
//
//    @Override
//    public Message find(UUID messageId) {
//        List<Message> list = messageList.values().stream().flatMap(List::stream).toList();
//
//        return CommonUtils.findById(list, messageId, Message::getMessageId)
//                .orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다."));
//    }
//
//    @Override
//    public List<Message> findAllByChannelId(UUID channelId) {
//        if (messageList.isEmpty()) {
//            throw new EmptyMessageListException("Repository 에 저장된 메시지 리스트가 없습니다.");
//        }
//        List<Message> messages = messageList.get(channelId);
//
//        if (messages.isEmpty()) {
//            throw new EmptyMessageListException("해당 채널에 저장된 메시지 리스트가 없습니다.");
//        }
//        return messages;
//    }
//
//    @Override
//    public List<Message> findAllByMessageId(UUID messageId) {
//        if (messageList.isEmpty()) {
//            throw new EmptyMessageListException("Repository 에 저장된 메시지 리스트가 없습니다.");
//        }
//        List<Message> messages = messageList.values().stream().flatMap(List::stream)
//                .filter(message -> message.getMessageId().equals(messageId))
//                .toList();
//
//        if (messages.isEmpty()) {
//            throw new EmptyMessageListException("해당 채널에 저장된 메시지 리스트가 없습니다.");
//        }
//        return messages;
//    }
//
//    @Override
//    public Message update(Message message,  UpdateMessageDTO updateMessageDTO) {
//        if (updateMessageDTO.newText() != null) {
//            message.setText(updateMessageDTO.newText());
//        }
//        fileRepository.save(messageList);
//        return message;
//    }
//
//    @Override
//    public void remove(UUID messageId) {
//        List<Message> messages = findAllByMessageId(messageId);
//        Message message = find(messageId);
//        messages.remove(message);
//        fileRepository.save(messageList);
//    }
}
