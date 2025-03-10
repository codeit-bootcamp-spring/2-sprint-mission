package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void createMessage(Message message);
    Optional<Message> selectMessageById(UUID id);
    List<Message> selectMessagesByChannel(UUID channelId);
    void updateMessage(UUID id, String content, UUID userId, UUID channelId);
    void deleteMessage(UUID id, UUID userId, UUID channelId);
}
