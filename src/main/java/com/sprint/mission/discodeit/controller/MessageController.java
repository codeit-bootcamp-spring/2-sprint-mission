package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.messagedto.MessageCreationDto;
import com.sprint.mission.discodeit.application.messagedto.MessageDto;
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

    public MessageDto createMessage(MessageCreationDto messageCreationDto, List<MultipartFile> files) {
        List<UUID> profileImageIds = files.stream()
                .map(binaryContentService::createProfileImage)
                .toList();

        return messageService.create(messageCreationDto, profileImageIds);
    }

    public List<MessageDto> findByChannelId(UUID channelId) {
        return messageService.findByChannelId(channelId);
    }
}