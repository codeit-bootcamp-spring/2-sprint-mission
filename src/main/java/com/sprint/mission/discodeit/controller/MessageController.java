package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController implements MessageApi {

  private final MessageService messageService;
  private final MessageMapper messageMapper;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> create(
          @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
          @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    log.info("▶▶ [API] Creating message - channelId: {}, authorId: {}",
            messageCreateRequest.channelId(), messageCreateRequest.authorId());

    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
            .map(files -> files.stream()
                    .map(file -> {
                      try {
                        log.debug("▶▶ [API] Processing attachment - fileName: {}", file.getOriginalFilename());
                        return new BinaryContentCreateRequest(
                                file.getOriginalFilename(),
                                file.getContentType(),
                                file.getBytes()
                        );
                      } catch (IOException e) {
                        log.error("◀◀ [API] Failed to process attachment - fileName: {}", file.getOriginalFilename(), e);
                        throw new RuntimeException(e);
                      }
                    })
                    .toList())
            .orElse(new ArrayList<>());

    MessageDto createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
    log.info("◀◀ [API] Message created - id: {}", createdMessage.id());
    return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
  }

  @PatchMapping(path = "{messageId}")
  public ResponseEntity<MessageDto> update(@PathVariable("messageId") UUID messageId,
                                           @RequestBody MessageUpdateRequest request) {
    log.info("▶▶ [API] Updating message - id: {}", messageId);
    MessageDto updatedMessage = messageService.update(messageId, request);
    log.info("◀◀ [API] Message updated - id: {}", messageId);
    return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
  }

  @DeleteMapping(path = "{messageId}")
  public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {
    log.info("▶▶ [API] Deleting message - id: {}", messageId);
    messageService.delete(messageId);
    log.info("◀◀ [API] Message deleted - id: {}", messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<List<MessageDto>> findAllByChannelId(
          @RequestParam("channelId") UUID channelId) {
    log.info("▶▶ [API] Fetching messages for channel - channelId: {}", channelId);
    List<MessageDto> messages = messageService.findAllByChannelId(channelId);
    log.info("◀◀ [API] Found {} messages for channel - channelId: {}", messages.size(), channelId);
    return ResponseEntity.status(HttpStatus.OK).body(messages);
  }

  @GetMapping("/channel/{channelId}")
  public ResponseEntity<PageResponse<MessageDto>> getMessagesByChannelIdWithPaging(
          @PathVariable UUID channelId,
          @RequestParam(defaultValue = "0") int page) {
    log.info("▶▶ [API] Fetching paged messages - channelId: {}, page: {}", channelId, page);
    PageResponse<MessageDto> response = messageService.findAllByChannelIdWithPaging(channelId, page);
    log.info("◀◀ [API] Returning {} messages (page {}) - channelId: {}",
            response.content().size(), page, channelId);
    return ResponseEntity.ok(response);
  }
}