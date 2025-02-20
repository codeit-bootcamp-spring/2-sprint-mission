package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface MessageService {
    Message createMessage(User sender, String content);
    List<Message> getMessages();
    List<Message> getMessagesBySenderId(Long userId);
    List<Message> getMessagesByChannelId(Long channelId);
    Message getMessageById(Long messageId);
    Message updateMessage(Long messageId, User sender, String content);
    boolean deleteMessage(Long messageId);
}
