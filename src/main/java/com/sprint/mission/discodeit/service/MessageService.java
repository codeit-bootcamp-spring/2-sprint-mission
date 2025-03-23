package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.MessageService.MessageCreateDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateDTO messageCreateDTO);
    List<Message> findByUser(UUID userId);
    List<Message> findByChannel(UUID channelId);
    List<Message> findByUserAndByChannel(UUID userId, UUID channelId);
    List<Message> findAll();
    Message update(UUID messageId, String newMessage);
    void delete(UUID messageId);
}
