package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(User sender, String content);
    List<Message> getMessages();
    List<Message> getMessagesBySenderId(UUID userId);
    List<Message> getMessagesByChannelId(UUID channelId);
    Message getMessageById(UUID messageId);
    Message updateMessage(UUID messageId, User sender, String content);
    boolean deleteMessage(UUID messageId);
}
