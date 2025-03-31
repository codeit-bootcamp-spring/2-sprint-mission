package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.MessageService.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageService.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody MessageCreateRequest request) {
        Message message = messageService.create(request, null); //미완성

        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
    public ResponseEntity<Message> update(@PathVariable UUID messageId , @RequestBody MessageUpdateRequest request) {
        Message message = messageService.update(messageId, request);

        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/find/{messageId}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable UUID messageId) {
        Message message = messageService.findById(messageId);

        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/messages/channel", method = RequestMethod.GET)
    public ResponseEntity<?> findByChannel(@RequestParam UUID channelId) {
        List<Message> messages = messageService.findByChannel(channelId);

        return ResponseEntity.ok(messages);
    }

    @RequestMapping(value = "/messages/user", method = RequestMethod.GET)
    public ResponseEntity<?> findByUser(@RequestParam UUID userId) {
        List<Message> messages = messageService.findByUser(userId);

        return ResponseEntity.ok(messages);
    }



}
