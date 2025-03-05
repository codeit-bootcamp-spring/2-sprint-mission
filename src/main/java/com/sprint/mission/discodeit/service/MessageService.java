package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message getMessage(UUID messageId);

    List<Message> getAllMessages();

    List<Message> getUpdatedMessages();

    void registerMessage(UUID channelId, String userName, String messageContent);

    void updateMessage(UUID messageId, String messageContent);

    void deleteMessage(UUID messageId);
}
