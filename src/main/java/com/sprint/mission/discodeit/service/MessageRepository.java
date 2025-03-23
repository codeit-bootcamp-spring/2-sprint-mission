package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    boolean register(Message message);
    Optional<Message> findById(UUID id);
    List<Message> findAll();
    List<Message> findAllByChannelId(UUID channelId);
    List<Message> findAllByAuthorId(UUID authorId);
    boolean updateMessage(Message message);
    boolean deleteMessage(UUID id);
}
