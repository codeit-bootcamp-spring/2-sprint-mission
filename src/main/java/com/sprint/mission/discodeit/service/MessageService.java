package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(MessageCreateRequest messageCreateDto);
    Message findById(UUID id);
    List<Message> findByChannelId(UUID id);
    void update(MessageUpdateRequest messageUpdateDto);
    void delete(UUID id);
}
