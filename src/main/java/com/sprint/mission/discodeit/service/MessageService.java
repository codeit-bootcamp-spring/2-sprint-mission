package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequestDto requestDto);
    Message find(UUID messageId);
    List<Message> findAll();
    Message update(UUID messageId, String newContent, List<UUID> newAttachmentIds);
    void delete(UUID messageId);
}
