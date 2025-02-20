package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();

    public void createMessage(Message message) { data.put(message.getId(), message); }
    public Message getMessage(UUID id) { return data.get(id); }
    public List<Message> getAllMessages() { return new ArrayList<>(data.values()); }
    public void updateMessage(UUID id, String content) {
        if (data.containsKey(id)) data.get(id).updateContent(content);
    }
    public void deleteMessage(UUID id) { data.remove(id); }
}
