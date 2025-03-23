package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.MessageUpdateDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createMessage(MessageCreateDto createDto);
    MessageResponseDto findById(UUID id);
    List<MessageResponseDto> findAllByChannelId(UUID channelId);
    void updateMessage(MessageUpdateDto updateDto);
    void deleteMessage(UUID id, UUID userId, UUID channelId);
}
