package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort.Direction;
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

  private static final Logger log = LoggerFactory.getLogger(MessageController.class);

  private final MessageService messageService;

  @GetMapping
  public ResponseEntity<PageResponse<MessageResponse>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId,
      @RequestParam(value = "cursor", required = false) Instant cursor,
      @PageableDefault(
          size = 50,
          page = 0,
          sort = "createdAt",
          direction = Direction.DESC
      ) Pageable pageable) {
    log.debug("GET /api/messages - 메시지 목록 조회 요청: channelId={}, cursor={}", channelId, cursor);

    PageResponse<MessageResponse> messages = messageService.findAllByChannelId(channelId, cursor,
        pageable);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponse> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachmentFiles) {
    log.info("POST /api/messages - 메시지 생성 요청: authorId={}, channelId={}",
        messageCreateRequest.authorId(), messageCreateRequest.channelId());
    if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
      log.info("첨부 파일 개수: {}", attachmentFiles.size());
      attachmentFiles.stream()
          .filter(file -> !file.isEmpty())
          .limit(3)
          .forEach(file ->
              log.debug("첨부 파일: filename={}, size={} bytes",
                  file.getOriginalFilename(), file.getSize()));
    }

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
    log.info("PATCH /api/messages/{} - 메시지 수정 요청: newContent={}",
        messageId, messageUpdateRequest.newContent());

    MessageResponse updatedMessage = messageService.update(messageId, messageUpdateRequest);
    return ResponseEntity.ok(updatedMessage);
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    log.info("DELETE /api/messages/{} - 메시지 삭제 요청", messageId);
    
    messageService.delete(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
