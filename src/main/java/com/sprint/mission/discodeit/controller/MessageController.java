package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
public class MessageController {

  private final BasicMessageService messageService;

  @PostMapping
  public ResponseEntity<Message> createMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart("binaryContents") Optional<List<MultipartFile>> binaryContentFiles)
      throws IOException {

    List<BinaryContentCreateRequest> binaryContents = binaryContentFiles.map(files -> {
      return files.stream().map(file -> {
        try {
          return new BinaryContentCreateRequest(
              file.getOriginalFilename(),
              file.getContentType(),
              file.getBytes()
          );
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }).toList();
    }).orElse(List.of());

    Message createdMessage = messageService.create(messageCreateRequest, binaryContents);

    return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<Message> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {

    Message updatedMessage = messageService.update(messageId, messageUpdateRequest);

    return new ResponseEntity<>(updatedMessage, HttpStatus.OK);
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable UUID messageId) {

    messageService.delete(messageId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/channels/{channelId}")
  public ResponseEntity<List<Message>> getMessagesByChannel(
      @PathVariable UUID channelId) {

    List<Message> messages = messageService.findAllByChannelId(channelId);

    return new ResponseEntity<>(messages, HttpStatus.OK);
  }
}
