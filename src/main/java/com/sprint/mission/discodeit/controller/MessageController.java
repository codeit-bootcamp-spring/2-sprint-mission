package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageCreateRequest request
    , @RequestParam Optional<BinaryContentCreateRequest> profileCreateRequest) {
        Message message = messageService.create(request, profileCreateRequest
                .stream().toList());
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequest request) {
        return ResponseEntity.ok(messageService.update(messageId, request));
    }

    @DeleteMapping("{channelId}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok("메세지가 삭제되었습니다");
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }
}
