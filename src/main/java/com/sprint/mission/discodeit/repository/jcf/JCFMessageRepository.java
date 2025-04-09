package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JCFMessageRepository implements MessageRepository {

    private static final Map<UUID, Message> messageDb = new HashMap<>();

    @Override
    public void save(Message message) {
        messageDb.put(message.getId(), message);
    }

    @Override
    public Message findByMessageId(UUID messageId) {
        return messageDb.get(messageId);
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
