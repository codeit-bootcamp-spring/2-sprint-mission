package com.sprint.mission.discodeit.adapter.outbound.message;

import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFMessageRepository implements MessageRepositoryPort {

  private Map<UUID, Message> messageList = new ConcurrentHashMap<>();
//    private Map<UUID, List<Message>> messageList = new ConcurrentHashMap<>();

  @Override
  public Message save(Message message) {
    messageList.put(message.getMessageId(), message);
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
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    this.findAllByChannelId(channelId)
        .forEach(message -> this.deleteById(message.getMessageId()));
  }

//    @Override
//    public Message save(Channel channel, Message message) {
//        List<Message> messages = messageList.getOrDefault(channel.getChannelId(), new ArrayList<>());
//        messages.add(message);
//        messageList.put(channel.getChannelId(), messages);
//        return message;
//    }
//
//    @Override
//    public Message find(UUID messageId) {
//        List<Message> list = messageList.values().stream().flatMap(List::stream).toList();
//        return CommonUtils.findById(list, messageId, Message::getMessageId)
//                .orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다."));
//    }
//
//    @Override
//    public List<Message> findAllByChannelId(UUID channelId) {
//        return messageList.get(channelId);
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

//
//    @Override
//    public void remove(UUID messageId) {
//        List<Message> messages = findAllByMessageId(messageId);
//        Message message = find(messageId);
//        messages.remove(message);
//
//        messages.remove(message);
//    }
}
