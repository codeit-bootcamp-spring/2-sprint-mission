package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResult create(MessageCreationRequest messageCreationRequest, List<UUID> attachmentsIds);

    MessageResult findById(UUID id);

    List<MessageResult> findAllByChannelId(UUID channelId);

    MessageResult updateContext(UUID id, String context);

    void delete(UUID id);
}
