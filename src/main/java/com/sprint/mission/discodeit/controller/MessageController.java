package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.LogMapUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Message Api")
public class MessageController {

  private static final Logger log = LoggerFactory.getLogger(MessageController.class);
  private final MessageService messageService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
            .toList())
        .orElse(new ArrayList<>());
    Message message = messageService.create(request, attachmentRequests);
    log.info("{}", LogMapUtil.of("action", "create")
        .add("message", message));

    return ResponseEntity.status(HttpStatus.CREATED).body(message);
  }

  @GetMapping
  public ResponseEntity<List<Message>> readAllByChannel(@RequestParam UUID channelId) {
    List<Message> messages = messageService.readAllByChannelKey(channelId);
    log.info("{}", LogMapUtil.of("action", "readAllByChannel")
        .add("messages", messages));

    return ResponseEntity.ok(messages);
  }

  @PutMapping("/{messageId}")
  public ResponseEntity<Message> update(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request) {
    Message updated = messageService.update(messageId, request);
    log.info("{}", LogMapUtil.of("action", "update")
        .add("updated", updated));

    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    log.info("{}", LogMapUtil.of("action", "delete")
        .add("messageId", messageId));
    return ResponseEntity.noContent().build();
  }
}
