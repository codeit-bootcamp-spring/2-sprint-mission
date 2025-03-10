package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(Message message, UUID channelId, UUID authorId);
    Message findById(UUID id);
    List<Message> findAll();
    void update(UUID id, String content, UUID channelId, UUID authorId);
    void delete(UUID id);
}
