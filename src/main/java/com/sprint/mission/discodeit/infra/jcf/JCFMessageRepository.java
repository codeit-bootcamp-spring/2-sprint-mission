package com.sprint.mission.discodeit.infra.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.infra.MessageRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private static final Map<UUID, Message> messages = new LinkedHashMap<>();

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);
        return findById(message.getId());
    }

    @Override
    public Message findById(UUID id) {
        Message message = messages.get(id);
        if (message == null) {
            throw new IllegalArgumentException("[ERROR] 해당 메세지가 없습니다");
        }

        return message;
    }

    @Override
    public List<Message> findAll() {
        return messages.values()
                .stream()
                .toList();
    }

    @Override
    public void updateContext(UUID id, String context) {
        messages.get(id)
                .updateContext(context);
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }
}
