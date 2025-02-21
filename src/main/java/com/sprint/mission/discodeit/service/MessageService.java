package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createMessage(Message message);
    User getMessage(UUID id);
    List<User> getAllMessage();
    void updateMessage(UUID id, Message message);
    void deleteMessage(UUID id);
}
