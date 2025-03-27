package com.sprint.mission.discodeit.updater.message;

import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MessageContentUpdater implements MessageUpdater {
    @Override
    public boolean supports(Message message, MessageUpdateRequest messageUpdateRequest) {
        return !message.getContent().equals(messageUpdateRequest.content());
    }

    @Override
    public void update(UUID messageId, MessageUpdateRequest request, MessageRepository messageRepository) {
        messageRepository.updateMessageContent(messageId, request.content());
    }
}
