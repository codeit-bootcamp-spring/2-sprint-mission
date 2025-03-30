package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    @PostMapping
    public ResponseEntity<MessageResult> createMessage(@RequestPart("message") MessageCreationRequest messageCreationRequest,
                                                       @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        List<UUID> profileImageIds = files.stream()
                .map(binaryContentService::createProfileImage)
                .toList();

        return ResponseEntity.ok(messageService.create(messageCreationRequest, profileImageIds));
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<MessageResult>> findByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResult> updateContent(@PathVariable UUID messageId, @RequestParam String context) {
        return ResponseEntity.ok(messageService.updateContext(messageId, context));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }
}