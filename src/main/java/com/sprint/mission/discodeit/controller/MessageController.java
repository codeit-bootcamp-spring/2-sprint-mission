package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @GetMapping
  public ResponseEntity<List<MessageResponse>> findAllMessagesByChannel(
      @RequestParam UUID channelId) {
    List<MessageResponse> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(messages);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponse> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachmentFiles) {

    List<BinaryContentCreateRequest> attachmentsCreateRequest = null;
    if (attachmentFiles != null) {
      attachmentsCreateRequest = attachmentFiles.stream()
          .filter(file -> !file.isEmpty())
          .map(BinaryContentCreateRequest::fromMultipartFile)
          .toList();
    }

    MessageResponse message = messageService.create(messageCreateRequest, attachmentsCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(message);
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageResponse> update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {
    MessageResponse updatedMessage = messageService.update(messageId, messageUpdateRequest);
    return ResponseEntity.ok(updatedMessage);
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
