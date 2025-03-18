package com.sprint.mission.discodeit.updater;

import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.stereotype.Component;

@Component
public class MessageAttachmentIdsUpdater implements MessageUpdater {
    @Override
    public boolean supports(Message message, MessageUpdateRequest messageUpdateRequest) {
        return !message.getAttachmentIds().equals(messageUpdateRequest.attachmentIds());
    }

    @Override
    public void update(Message message, MessageUpdateRequest request, MessageRepository messageRepository, BinaryContentService binaryContentService) {
        messageRepository.updateAttachmentIds(message.getId(), request.attachmentIds());
    }
}
