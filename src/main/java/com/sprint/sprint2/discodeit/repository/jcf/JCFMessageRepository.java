package com.sprint.sprint2.discodeit.repository.jcf;

import com.sprint.sprint2.discodeit.entity.Message;
import com.sprint.sprint2.discodeit.repository.MessageRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data;

    public JCFMessageRepository(Map<UUID, Message> data) {
        this.data = data;
    }

    @Override
    public Message findById(String uuId) {
        return Optional.ofNullable(data.get(UUID.fromString(uuId))).orElseThrow(() -> new NullPointerException("없는 메세지 입니다. "));
    }

    @Override
    public List<Message> findByAll() {
        return data.values().stream().toList();
    }

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public void delete(UUID uuId) {
        data.remove(uuId);
    }
}
