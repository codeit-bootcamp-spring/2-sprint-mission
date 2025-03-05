package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageRepository {
    void create(Message message);
    void update(Message message);
    void delete(UUID id);
}
