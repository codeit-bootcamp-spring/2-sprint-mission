package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResult create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> profileImages);

    MessageResult getById(UUID id);

    List<MessageResult> getAllByChannelId(UUID channelId);

    MessageResult updateContext(UUID id, String context);

    void delete(UUID id);
}
