package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.Message.MessageUpdateDTO;
import com.sprint.mission.discodeit.Exception.MessageNotFoundException;
import com.sprint.mission.discodeit.Repository.MessageRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

    }

    @Override
    public Message find(UUID messageId) {
        Message message = messageList.values().stream().flatMap(List::stream)
                .filter(s -> s.getMessageId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new MessageNotFoundException("해당 ID를 가지는 메시지를 찾을 수 없습니다."));
        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageList.get(channelId);
        return messages;
    }

    @Override
    public UUID update(Message message, MessageUpdateDTO messageUpdateDTO) {
        if (messageUpdateDTO.replaceText() != null) {
            message.setText(messageUpdateDTO.replaceText());
        }
        return message.getMessageId();
    }

    @Override
    public void remove(Channel channel, Message message) {
        List<Message> messages = messageList.get(channel.getChannelId());
        messages.remove(message);

    }
}
