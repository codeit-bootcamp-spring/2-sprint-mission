package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);
    Optional<Message> findById(UUID id);
    Optional<List<Message>> findAll();
    Optional<List<Message>> findByChannelId(UUID channelId);
    void update(Message message);
    void delete(UUID id);
    void deleteChannelById(UUID id);
}
