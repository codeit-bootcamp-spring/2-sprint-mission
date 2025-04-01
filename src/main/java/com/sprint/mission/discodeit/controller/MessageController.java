package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.LogMapUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Message> create(@RequestBody MessageCreateRequest messageRequest) {
        Message message = messageService.create(messageRequest);
        log.info("{}", LogMapUtil.of("action", "create")
                .add("message", message));

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public ResponseEntity<Message> read(@RequestParam UUID messageKey) {
        Message message = messageService.read(messageKey);
        log.info("{}", LogMapUtil.of("action", "read")
                .add("message", message));

        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/readAllByChannel", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> readAllByChannel(@RequestParam UUID channelKey) {
        List<Message> messages = messageService.readAllByChannelKey(channelKey);
        log.info("{}", LogMapUtil.of("action", "readAllByChannel")
                .add("messages", messages));

        return ResponseEntity.ok(messages);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<Message> update(@RequestBody MessageUpdateRequest request) {
        Message updated = messageService.update(request);
        log.info("{}", LogMapUtil.of("action", "update")
                .add("updated", updated));

        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam UUID messageKey) {
        messageService.delete(messageKey);
        log.info("{}", LogMapUtil.of("action", "delete")
                .add("messageKey", messageKey));
        return ResponseEntity.noContent().build();
    }
}
