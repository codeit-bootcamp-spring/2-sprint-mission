package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFMessageRepository implements MessageRepository {
    private static volatile JCFMessageRepository instance;
    private final Map<UUID, Message> data;

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

    private JCFMessageRepository() {
        this.data = new ConcurrentHashMap<>();
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }


    @Override
    public void deleteById(UUID messageId) {
        data.remove(messageId);
    }
}
