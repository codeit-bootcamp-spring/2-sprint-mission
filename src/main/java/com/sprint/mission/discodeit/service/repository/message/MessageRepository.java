package com.sprint.mission.discodeit.service.repository.message;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {

    void save(Message user);
    Message findByMessageId(UUID messageId);
    List<Message> findAll();
    Message delete(UUID id);
    void clearDb();
}
