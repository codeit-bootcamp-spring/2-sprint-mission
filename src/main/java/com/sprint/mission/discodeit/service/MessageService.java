package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(UUID authorId, UUID channelId, String content);

    Message findById(UUID messageId);

    List<Message> findByChannelId(UUID channelId);

    List<Message> findByAuthorId(UUID authorId);

    List<Message> findAll();

    Message update(UUID messageId, String newContent);

    void delete(UUID messageId);
}
