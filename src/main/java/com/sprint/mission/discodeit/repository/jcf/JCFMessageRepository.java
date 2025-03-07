package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    public final List<Message> messageData;
    public UserService userService;

    public JCFMessageRepository() {
        messageData = new ArrayList<>();
    }

    @Override
    public void save(Message message) {
        messageData.add(message);
    }

    @Override
    public List<Message> load() {
        return messageData.stream().toList();
    }

    @Override
    public void deleteFromFile(Message message) {
        messageData.remove(message);
    }
}
