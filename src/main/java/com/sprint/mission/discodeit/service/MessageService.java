package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Message.CreateMessageDto;
import com.sprint.mission.discodeit.DTO.Message.MessageDto;
import com.sprint.mission.discodeit.DTO.Message.UpdateMessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(CreateMessageDto dto);
    MessageDto find(UUID messageId);
    List<MessageDto> findAllByChannelId(UUID channelId);
    List<MessageDto> findAllByAuthorId(UUID authorId);
    MessageDto update(UpdateMessageDto dto);
    void delete(UUID messageId);
}