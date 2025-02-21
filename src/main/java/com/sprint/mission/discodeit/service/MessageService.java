package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    Message saveMessage(String text);
    void findAll();
    void findById(UUID id);
    void delete(UUID id);
    void update(UUID id, String message);
}
