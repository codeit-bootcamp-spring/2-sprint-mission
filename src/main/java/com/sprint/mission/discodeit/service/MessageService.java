package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.MessageDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(String context, UUID channelId, UUID userId);

    MessageDto findById(UUID id);

    List<MessageDto> findAll();

    List<MessageDto> findByChannelId(UUID channelId);

    void updateContext(UUID id, String context);

    void delete(UUID id);
}
