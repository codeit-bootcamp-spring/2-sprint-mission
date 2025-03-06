package com.sprint.mission.discodeit.repository.jcf;

import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messages = new LinkedHashMap<>();

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);
        return findById(message.getId());
    }

    @Override
    public Message findById(UUID id) {
        Message message = messages.get(id);
        if (message == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent());
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
