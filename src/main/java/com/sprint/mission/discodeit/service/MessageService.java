package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message getMessage(UUID mid);
    List<Message> getAllMessage();
    void registerMessage(UUID cid, String userName, String messageContent);
    void updateMessage(UUID mid, String messageContent);
    void deleteMessage(UUID mid);
}
