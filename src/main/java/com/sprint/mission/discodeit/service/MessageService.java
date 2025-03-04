package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message create(UUID userId, UUID channelId, String content);
    Message findById(UUID id);
    List<Message> findByChannelId(UUID channelId);
    List<Message> findByUserId(UUID userId);
    List<Message> findAll();
    void updateContent(UUID id, String content);
    void delete(UUID id);
}
