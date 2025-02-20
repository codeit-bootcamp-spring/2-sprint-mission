package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void create(Message message);
    Optional<Message> findById(UUID id);
    List<Message> findAll();
    void update(UUID id);
    void delete(UUID id);
}
