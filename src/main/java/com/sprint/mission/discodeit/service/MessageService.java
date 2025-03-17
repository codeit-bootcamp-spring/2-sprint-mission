package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.messageDto.MessageCreateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequest request);
    Message find(UUID messageId);
    List<Message> findAll();
    Message update(UUID messageId, String newContent);
    void delete(UUID messageId);
}
