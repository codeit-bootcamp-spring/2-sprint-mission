package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void sendMessage(UUID channelId,UUID UserId, String content);
    Message findMessageById(UUID id);
    List<Message> findAllMessages();
    List<Message> findMessageByChannelId(UUID id);
    void updateMessage(UUID id, String content);
    void deleteMessageById(UUID id);
}
