package com.sprint.mission.discodeit.updater.message;

import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.UUID;

public interface MessageUpdater {
    boolean supports(Message message, MessageUpdateRequest messageUpdateRequest);
    void update(UUID messageId, MessageUpdateRequest request, MessageRepository messageRepository);
}
