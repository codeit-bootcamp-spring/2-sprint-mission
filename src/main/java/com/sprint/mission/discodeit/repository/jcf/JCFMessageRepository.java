package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class JCFMessageRepository implements MessageRepository {

    private static final Map<UUID, Message> messageDb = new HashMap<>();

    @Override
    public void save(Message message) {
        messageDb.put(message.getId(), message);
    }

    @Override
    public Message findByMessageId(UUID messageId) {
        return Optional.ofNullable(messageDb.get(messageId))
            .orElseThrow(
                () -> new IllegalArgumentException("[ERROR]유효하지 않은 아이디 입니다. id : " + messageId));
    }

    @Override
    public void update(UUID messageId, UpdateMessageRequest request) {
        Message message = findByMessageId(messageId);
        message.updateContent(request.getContent());
    }

    @Override
    public List<Message> findAll() {
        return messageDb.values().stream()
            .sorted(Comparator.comparing(Message::getCreatedAt)).toList();
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageDb.values().stream()
            .filter(m -> m.getChannelId().equals(channelId)).toList();
    }

    @Override
    public void delete(UUID messageId) {
        validMessageId(messageId);
        messageDb.remove(messageId);
    }

    private void validMessageId(UUID messageId) {
        if (!messageDb.containsKey(messageId)) {
            throw new NoSuchElementException("[ERROR]Message ID Error");
        }
    }
}
