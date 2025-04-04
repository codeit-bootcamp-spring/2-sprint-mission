package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public Message createMessage(@RequestBody MessageCreateRequest request) {
        return messageService.create(request, List.of());
    }

    @PutMapping("/{messageId}")
    public Message updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequest request) {
        return messageService.update(messageId, request);
    }

    @DeleteMapping("/{messageId}")
    public void deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
    }

    @GetMapping("/channel/{channelId}")
    public List<Message> getMessagesByChannel(@PathVariable UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }
}
