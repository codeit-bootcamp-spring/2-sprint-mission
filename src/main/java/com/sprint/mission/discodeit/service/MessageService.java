package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ChannelMessage;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.PrivateMessage;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    PrivateMessage sendPrivateMessage(UUID senderId, String content, UUID receiverId);
    ChannelMessage sendChannelMessage(UUID senderId, String content, UUID channelId);
    List<Message> getAllMessages();
    List<Message> getMessagesBySenderId(UUID senderId);
    List<ChannelMessage> getChannelMessagesByChannelId(UUID channelId);
    List<PrivateMessage> getPrivateMessagesByReceiverId(UUID receiverId);
    Message getMessageById(UUID messageId);
    Message updateMessage(UUID messageId, String content);
    boolean deleteMessage(UUID messageId);
}
