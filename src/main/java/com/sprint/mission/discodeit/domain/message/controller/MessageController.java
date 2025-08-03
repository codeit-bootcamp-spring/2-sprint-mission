package com.sprint.mission.discodeit.domain.message.controller;

import com.sprint.mission.discodeit.common.dto.response.PageResponse;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.message.dto.MessageResult;
import com.sprint.mission.discodeit.domain.message.dto.request.ChannelMessagePageRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResult> create(
      @Valid @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentRequest> binaryContentRequests = getBinaryContentRequests(attachments);
    MessageResult message = messageService.create(messageCreateRequest, binaryContentRequests);

    return ResponseEntity.ok(message);
  }

  @GetMapping
  public ResponseEntity<PageResponse<MessageResult>> getAllByChannelId(
      @RequestParam("channelId") UUID channelId,
      @RequestParam(value = "cursor", required = false) Instant cursor,
      @PageableDefault(size = 50, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    ChannelMessagePageRequest messageByChannelRequest = new ChannelMessagePageRequest(cursor,
        pageable.getPageSize(), pageable.getPageNumber(), pageable.getSort());
    PageResponse<MessageResult> messages = messageService.getAllByChannelId(channelId,
        messageByChannelRequest);

    return ResponseEntity.ok(messages);
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageResult> update(@PathVariable UUID messageId,
      @Valid @RequestBody MessageUpdateRequest messageUpdateRequest) {
    MessageResult message = messageService.updateContext(messageId,
        messageUpdateRequest.newContent());

    return ResponseEntity.ok(message);
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);

    return ResponseEntity.noContent().build();
  }

  private List<BinaryContentRequest> getBinaryContentRequests(List<MultipartFile> attachments) {
    if (attachments == null) {
      return List.of();
    }
    return attachments.stream()
        .map(BinaryContentRequest::from)
        .toList();
  }

}