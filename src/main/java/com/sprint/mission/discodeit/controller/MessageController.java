package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class MessageController implements MessageApi {

  private final BasicMessageService messageService;

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> binaryContentFiles) {

    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(binaryContentFiles)
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

    MessageDto createdMessage = messageService.create(messageCreateRequest, attachmentRequests);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdMessage);
  }

  @Override
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> update(
      @PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {

    MessageDto updatedMessage = messageService.update(messageId, messageUpdateRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedMessage);
  }

  @Override
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(
      @PathVariable("messageId") UUID messageId) {

    messageService.delete(messageId);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  @GetMapping
  public ResponseEntity<List<MessageDto>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId) {

    List<MessageDto> messages = messageService.findAllByChannelId(channelId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }
}
