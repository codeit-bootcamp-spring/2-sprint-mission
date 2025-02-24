package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createMessage(Message message);
    Message getMessage(Message sender);
    List<Message> getAllMessage();
    void updateMessage(String sender, String changeMessage);
    void deleteMessage(String sender);
}
