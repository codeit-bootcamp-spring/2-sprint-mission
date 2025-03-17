package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.Message.MessageCRUDDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.MessageRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFMessageRepository implements MessageRepository {
    private Map<UUID, List<Message>> messageList = new ConcurrentHashMap<>();

    @Override
    public void reset() {
        messageList = new ConcurrentHashMap<>();
    }

    @Override
    public void save(Channel channel, Message message) {
        List<Message> messages = messageList.getOrDefault(channel.getChannelId(), new ArrayList<>());
        messages.add(message);
        messageList.put(channel.getChannelId(), messages);

    }

    @Override
    public Message find(UUID messageId) {
        List<Message> list = messageList.values().stream().flatMap(List::stream).toList();
        Message message = CommonUtils.findById(list, messageId, Message::getMessageId)
                .orElseThrow(() -> CommonExceptions.MESSAGE_NOT_FOUND);
        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (messageList.isEmpty()) {
            throw CommonExceptions.EMPTY_MESSAGE_LIST;
        }
        List<Message> messages = messageList.get(channelId);

        if (messages.isEmpty()) {
            throw CommonExceptions.EMPTY_MESSAGE_LIST;
        }
        return messages;
    }

    @Override
    public UUID update(Message message, MessageCRUDDTO messageUpdateDTO) {
        if (messageUpdateDTO.text() != null) {
            message.setText(messageUpdateDTO.text());
        }
        if (messageUpdateDTO.messageId() != null) {
            message.setMessageId(messageUpdateDTO.messageId());
        }
        return message.getMessageId();
    }

    @Override
    public void remove(Channel channel, Message message) {
        List<Message> messages = findAllByChannelId(channel.getChannelId());
        messages.remove(message);
    }
}
