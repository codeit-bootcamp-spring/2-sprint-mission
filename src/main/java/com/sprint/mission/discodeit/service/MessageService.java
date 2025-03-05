package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    UUID create(String content, UUID userUuid, UUID channelUuid);
    Message read(UUID channelUuid);
    List<Message> readAll(UUID channelUuid);
    UUID update(int messageId, String content);
    void delete(int messageId);
}
