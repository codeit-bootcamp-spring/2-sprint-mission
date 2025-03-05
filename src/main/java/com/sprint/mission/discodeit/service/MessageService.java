package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.message.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message sendMessage(UUID senderId, String content, UUID channelId);
    List<Message> getAllMessages();
    Message getMessageByMessageId(UUID messageId);
    Message updateMessage(UUID messageId, String content);
    boolean deleteMessage(UUID messageId);
}
