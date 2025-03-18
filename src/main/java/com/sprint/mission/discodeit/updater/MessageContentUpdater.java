package com.sprint.mission.discodeit.updater;

import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.stereotype.Component;

@Component
public class MessageContentUpdater implements MessageUpdater {
    @Override
    public boolean supports(Message message, MessageUpdateRequest messageUpdateRequest) {
        return !message.getContent().equals(messageUpdateRequest.content());
    }

    @Override
    public void update(Message message, MessageUpdateRequest request, MessageRepository messageRepository) {
        messageRepository.updateMessageContent(message.getId(), request.content());
    }
}
