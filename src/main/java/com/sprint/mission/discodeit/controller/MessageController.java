package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
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
    public ResponseEntity<?> create(@RequestBody MessageCreateRequest messageRequest) {
        Message message = messageService.create(messageRequest);
        log.info("메시지 생성 완료 {}", message);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestParam UUID messageKey) {
        Message message = messageService.read(messageKey);
        log.info("메시지 조회 완료 {}", message);

        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/readAllByChannel", method = RequestMethod.GET)
    public ResponseEntity<?> readAllByChannel(@RequestParam UUID channelKey) {
        List<Message> messages = messageService.readAllByChannelKey(channelKey);
        log.info("채널 {} 의 메시지 {}", channelKey, messages);

        return ResponseEntity.ok(messages);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody MessageUpdateRequest request) {
        Message updated = messageService.update(request);
        log.info("메시지 수정 완료 {}", updated);

        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam UUID messageKey) {
        messageService.delete(messageKey);
        log.info("메시지 삭제 완료 {}", messageKey);
        return ResponseEntity.noContent().build();
    }
}
