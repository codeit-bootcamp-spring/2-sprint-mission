package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageList = new HashMap<>();

    @Override
    public Message save(Message message) {
        messageList.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findMessageById(UUID messageUUID) {
        return Optional.of(messageList.get(messageUUID));
    }

    @Override
    public List<Message> findAllMessage() {
        return messageList.values().stream().toList();
    }

    @Override
    public List<Message> findMessageByChannel(UUID channelUUID) {
        return messageList.values().stream()
                .filter(message -> message.getChannelUUID().equals(channelUUID))
                .toList();
    }

    @Override
    public void delete(UUID messageUUID) {
        messageList.remove(messageUUID);
    }
}
