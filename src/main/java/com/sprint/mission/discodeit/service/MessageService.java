package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message create(String content, UUID userId, UUID senderId);
    Message createMessage(MessageCreateRequestDTO dto);
    Optional<Message> find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message updateMessage(MessageUpdateRequestDTO dto);

    Message updateMessage(UUID messageId, String newContent);

    List<Message> findAll();

    Message update(UUID messageId, String newContent);

    void delete(UUID messageId);
}
