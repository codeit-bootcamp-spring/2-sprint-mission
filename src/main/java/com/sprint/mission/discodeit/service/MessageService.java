package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.application.dto.message.MessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(MessageCreationDto messageCreationDto, List<UUID> attachmentsIds);

    MessageDto findById(UUID id);

    List<MessageDto> findAllByChannelId(UUID channelId);

    MessageDto updateContext(UUID id, String context);

    void delete(UUID id);
}
