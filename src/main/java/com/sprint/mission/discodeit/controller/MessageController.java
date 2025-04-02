package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/message")
public class MessageController {

  private final MessageService messageService;

  @PostMapping(
      path = "/api/channels/{channelId}/messages",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<MessageDto> createMessage(
      @PathVariable UUID channelId,
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
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
    Message created = messageService.create(request, attachmentRequests);
    return ResponseEntity.status(HttpStatus.CREATED).body(MessageDto.from(created));
  }

  @PatchMapping("/api/messages/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request
  ) {
    Message updatedMessage = messageService.update(messageId, request);
    return ResponseEntity.ok(MessageDto.from(updatedMessage));
  }

  @DeleteMapping("/api/messages/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build(); //204
  }

  @GetMapping("/api/messages")
  public ResponseEntity<List<MessageDto>> getMessagesByChannelId(
      @RequestParam("channelId") UUID channelId
  ) {
    List<MessageDto> messages = messageService.findAllByChannelId(channelId).stream()
        .map(MessageDto::from)
        .toList();
    return ResponseEntity.ok(messages);
  }
}
