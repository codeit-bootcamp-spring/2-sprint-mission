package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageData;
    private static JCFMessageRepository instance = null;

    public static JCFMessageRepository getInstance() {
        if (instance == null) {
            instance = new JCFMessageRepository();
        }
        return instance;
    }

    private JCFMessageRepository() {
        this.messageData = new HashMap<>();
    }

    @Override
    public void save(Message message) {
        messageData.put(message.getId(), message);
    }

    @Override
    public Message findById(UUID id) {
        return messageData.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageData.values());
    }

    @Override
    public void delete(UUID id) {
        messageData.remove(id);
    }

    @Override
    public void update(Message message) {
        // 시간 갱신 (혹은 다른 필드 변경 시 필요한 로직)
        message.updateTime(System.currentTimeMillis());
        messageData.put(message.getId(), message);  // 변경된 데이터를 다시 저장
    }

    @Override
    public boolean existsById(UUID id) {
        return messageData.containsKey(id);
    }
}
