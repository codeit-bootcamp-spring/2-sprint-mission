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
        return null;
    }

    @Override
    public Message findById(UUID messageId) {
        return null;
    }

    @Override
    public List<Message> findAll() {
        return List.of();
    }

    @Override
    public List<Message> findBySenderId(UUID senderId) {
        return List.of();
    }


    @Override
    public Message update(UUID messageId, String content) {
        return null;
    }

    @Override
    public boolean delete(UUID messageId) {
        return false;
    }
}
