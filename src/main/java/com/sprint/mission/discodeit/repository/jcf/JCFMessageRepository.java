package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final List<Message> messageData;

    public JCFMessageRepository() {
        messageData = new ArrayList<>();
    }

    @Override
    public Message save(Message message) {
        messageData.add(message);
        return message;
    }

    @Override
    public List<Message> load() {
        return messageData.stream().toList();
    }

    @Override
    public void remove(Message message) {
        messageData.remove(message);
    }

    @Override
    public Optional<Message> loadToId(UUID id) {
        return messageData.stream().filter(m -> m.getId().equals(id)).findFirst();
    }
}
