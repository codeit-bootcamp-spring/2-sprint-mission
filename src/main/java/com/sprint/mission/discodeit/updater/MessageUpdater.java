package com.sprint.mission.discodeit.updater;

import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

public interface MessageUpdater {
    boolean supports(Message message, MessageUpdateRequest messageUpdateRequest);
    void update(Message message, MessageUpdateRequest request, MessageRepository messageRepository);
}
