package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private static volatile JCFMessageRepository instance;
    private final Map<UUID, Message> data = new HashMap<>();

    private JCFMessageRepository() {}

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
    public void save(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID messageId) {
        data.remove(messageId);
    }

    @Override
    public void update(UUID messageId, String content) {
        findById(messageId).ifPresent(message -> {
            message.update(content, System.currentTimeMillis());
            save(message);
        });
    }
}
