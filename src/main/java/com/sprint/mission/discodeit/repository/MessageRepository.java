package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    List<Message> findAll();

    Message findById(UUID messageId);

    void delete(UUID messageId);

    Map<UUID, Message> getMessageData();

    Message update(Message message, String newContent);
}
