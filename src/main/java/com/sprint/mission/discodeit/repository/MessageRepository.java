package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message findById(UUID messageId);

    List<Message> findAll();

    List<Message> findUpdatedMessages();

    void createMessage(Channel channel, User user, String messageContent);

    void updateMessage(UUID messageId, String messageContent);

    void deleteMessage(UUID messageId);

    void deleteMessagesByChannelId(UUID channelId);
}
