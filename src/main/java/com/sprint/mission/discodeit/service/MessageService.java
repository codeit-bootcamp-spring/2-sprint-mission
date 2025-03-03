package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface MessageService {
    void sendMessage(UUID channelId,UUID UserId, String content);
    void findMessageById(UUID id);
    void findAllMessages();
    void findMessageByChannelId(UUID id);
    void updateMessage(UUID id, String content);
    void deleteMessageById(UUID id);
}
