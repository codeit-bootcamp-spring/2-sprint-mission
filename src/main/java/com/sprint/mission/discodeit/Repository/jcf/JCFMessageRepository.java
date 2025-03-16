package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.Exception.EmptyMessageListException;
import com.sprint.mission.discodeit.Exception.MessageNotFoundException;
import com.sprint.mission.discodeit.Repository.MessageRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFMessageRepository implements MessageRepository {
    private Map<UUID, List<Message>> messageList = new ConcurrentHashMap<>();
    @Override
    public void reset() {
        messageList = new ConcurrentHashMap<>();
    }
    @Override
    public void saveMessage(Message message) {
        messageList.computeIfAbsent(message.getChannelId(), k -> new ArrayList<>()).add(message);
    }

    @Override
    public List<Message> findMessageListByChannel(Channel channel) {
        return findMessageListByChannel(channel.getChannelId());
    }

    @Override
    public List<Message> findMessageListByChannel(UUID channelId) {
        List<Message> messages = Optional.ofNullable(messageList.get(channelId))
                .orElseThrow(() -> new EmptyMessageListException("메시지함이 비어있습니다."));
        return messages;
    }

    @Override
    public Message findMessageByChannel(Channel channel, UUID messageId) {
        List<Message> messages = findMessageListByChannel(channel);
        Message findMessage = messages.stream().filter(m -> m.getMessageId().equals(messageId))
                .findFirst().orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다."));
        return findMessage;
    }

    @Override
    public UUID updateMessage(Channel channel, Message message, String replaceText) {
        Message findMessage = findMessageByChannel(channel, message.getMessageId());
        findMessage.setText(replaceText);
        return findMessage.getMessageId();
    }

    @Override
    public UUID removeMessage(Channel channel,  Message message) {
        List<Message> messages = findMessageListByChannel(channel);
        Message findMessage = findMessageByChannel(channel, message.getMessageId());

        messages.remove(findMessage);
        messageList.put(channel.getChannelId(), messages);

        return findMessage.getMessageId();

    }
}
