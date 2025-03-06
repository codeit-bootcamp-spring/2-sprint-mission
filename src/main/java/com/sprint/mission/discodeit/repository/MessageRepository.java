package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    List<Message> findAll();

    Message findById(UUID messageId);

    List<Message> findByChannelId(UUID channelId);

    List<Message> findByAuthorId(UUID authorId);

    void delete(UUID messageId);
}
