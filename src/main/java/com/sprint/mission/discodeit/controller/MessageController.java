package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import java.util.List;
import java.util.UUID;

public class MessageController {
    private final MessageService messageService = JCFMessageService.getInstance();

    public MessageDto createMessage(String context, UUID chanelId, UUID userId) {
        return messageService.create(context, chanelId, userId);
    }

    public List<MessageDto> findByChannelId(UUID channelId){
        return messageService.findByChannelId(channelId);
    }
}