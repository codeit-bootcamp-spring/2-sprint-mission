package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping
  public ResponseEntity<MessageResult> createMessage(
      @Valid @RequestPart("message") MessageCreationRequest messageCreationRequest,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {

    return ResponseEntity.ok(messageService.create(messageCreationRequest, files));
  }

  @GetMapping("/channel/{channelId}")
  public ResponseEntity<List<MessageResult>> getByChannelId(@PathVariable UUID channelId) {
    return ResponseEntity.ok(messageService.getAllByChannelId(channelId));
  }

  @PutMapping("/{messageId}")
  public ResponseEntity<MessageResult> updateContent(@PathVariable UUID messageId,
      @RequestBody MessageCreationRequest messageCreationRequest) {
    return ResponseEntity.ok(
        messageService.updateContext(messageId, messageCreationRequest.context()));
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }
}