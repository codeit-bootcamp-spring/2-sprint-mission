package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.Message.MessageDto;
import com.sprint.mission.discodeit.dto.Message.UpdateMessageDto;

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