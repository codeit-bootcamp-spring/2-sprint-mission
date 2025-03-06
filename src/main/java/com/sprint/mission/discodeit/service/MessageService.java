package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String message, UUID userId, UUID channelId);
    List<Message> readByUser(UUID userId);
    List<Message> readByChannel(UUID channelId);
    List<Message> readByUserAndByChannel(UUID userId, UUID channelId);
    List<Message> readAll();
    Message update(UUID messageId, String newMessage);
    void delete(UUID messageId);
}
