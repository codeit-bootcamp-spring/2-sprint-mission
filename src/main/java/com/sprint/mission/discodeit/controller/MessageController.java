package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    public MessageResult createMessage(MessageCreationRequest messageCreationRequest, List<MultipartFile> files) {
        List<UUID> profileImageIds = files.stream()
                .map(binaryContentService::createProfileImage)
                .toList();

        return messageService.create(messageCreationRequest, profileImageIds);
    }

    public List<MessageResult> findByChannelId(UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }

    public MessageResult updateContent(UUID messageId, String context) {
        return messageService.updateContext(messageId, context);
    }

    public void delete(UUID messageId) {
        messageService.delete(messageId);
    }
}