package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(MessageCreateDto messageCreateDto);
    Message findById(UUID id);
    List<Message> findByChannelId(UUID id);
    void update(MessageUpdateDto messageUpdateDto);
    void delete(UUID id);
}
