package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message sendMessage(Message message);
    Message findByMessageId(UUID messageId);
    void deleteMessage(UUID messageId);

    List<Message> findByChannelId(UUID channelId);
    List<Message> findByUserId(UUID userId);
}
