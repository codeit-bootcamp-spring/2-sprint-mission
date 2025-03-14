package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;


public interface MessageService {
    Message create(String message, UUID channelId, UUID senderId);
    Message getMessage(UUID messageId);
    List<Message> getAllMessage();
    Message update(UUID messageId, String changeMessage);
    void delete(UUID messageId);
}
