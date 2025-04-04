package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResult> createMessage(
            @Valid @RequestPart("message") MessageCreationRequest messageCreationRequest,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

        return ResponseEntity.ok(messageService.create(messageCreationRequest, attachments));
    }

    @GetMapping
    public ResponseEntity<List<MessageResult>> getAllByChannelId(@RequestParam UUID channelId) {
        return ResponseEntity.ok(messageService.getAllByChannelId(channelId));
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<MessageResult> updateContent(@PathVariable UUID messageId,
                                                       @RequestBody String context) {
        return ResponseEntity.ok(
                messageService.updateContext(messageId, context));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);

        return ResponseEntity.noContent().build();
    }
}