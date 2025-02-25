package com.sprint.mission.discodeit.infra;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    Message findById(UUID id);

    List<Message> findAll();

    void updateContext(UUID id, String context);

    void delete(UUID id);
}
