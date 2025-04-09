package com.sprint.discodeit.sprint4.repository.jcf;

import com.sprint.discodeit.sprint5.domain.entity.Message;
import com.sprint.discodeit.sprint5.repository.file.MessageRepository;
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
    public Optional<Message> findById(UUID uuId) {
        return Optional.ofNullable(data.get(uuId));
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
