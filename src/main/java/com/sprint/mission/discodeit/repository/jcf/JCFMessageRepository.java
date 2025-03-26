package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.update.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.Empty.EmptyMessageListException;
import com.sprint.mission.discodeit.exception.NotFound.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.CommonUtils;
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
    public Message save(Channel channel, Message message) {
        List<Message> messages = messageList.getOrDefault(channel.getChannelId(), new ArrayList<>());
        messages.add(message);
        messageList.put(channel.getChannelId(), messages);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        List<Message> list = messageList.values().stream().flatMap(List::stream).toList();
        return CommonUtils.findById(list, messageId, Message::getMessageId)
                .orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다."));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageList.get(channelId);
        return messages;
    }

    @Override
    public List<Message> findAllByMessageId(UUID messageId) {
        if (messageList.isEmpty()) {
            throw new EmptyMessageListException("Repository 에 저장된 메시지 리스트가 없습니다.");
        }
        List<Message> messages = messageList.values().stream().flatMap(List::stream)
                .filter(message -> message.getMessageId().equals(messageId))
                .toList();

        if (messages.isEmpty()) {
            throw new EmptyMessageListException("해당 채널에 저장된 메시지 리스트가 없습니다.");
        }
        return messages;
    }

    @Override
    public Message update(Message message,  UpdateMessageDTO updateMessageDTO) {
        if (updateMessageDTO.replaceText() != null) {
            message.setText(updateMessageDTO.replaceText());
        }
        return message;
    }

    @Override
    public void remove(UUID messageId) {
        List<Message> messages = findAllByMessageId(messageId);
        Message message = find(messageId);
        messages.remove(message);

        messages.remove(message);
    }
}
