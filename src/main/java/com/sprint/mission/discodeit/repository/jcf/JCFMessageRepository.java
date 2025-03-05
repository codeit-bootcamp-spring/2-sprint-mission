package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private static volatile JCFMessageRepository instance;

    private final Map<UUID, Message> messages;


    private JCFMessageRepository() {
        messages = new HashMap<>();
    }

    public static JCFMessageRepository getInstance() {
        if (instance == null) {
            synchronized (JCFMessageRepository.class) {
                if (instance == null) {
                    instance = new JCFMessageRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }


    @Override
    public void delete(UUID messageId) {
        messages.remove(messageId);
    }

    @Override
    public boolean exists(UUID messageId) {
        return messages.containsKey(messageId);
    }
}
