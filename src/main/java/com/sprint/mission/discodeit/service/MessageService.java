package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message getMessage(UUID mid);
    List<Message> getAllMessages();
    void registerMessage(UUID cid, String userName, String messageContent);
}
