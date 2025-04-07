package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "메시지 생성")
  @ApiResponse(
      responseCode = "201",
      description = "메시지 생성 성공"
  )
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponse> createMessage(
      @RequestPart("messageCreateRequest") CreateMessageRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    UUID messageId = messageService.createMessage(request, attachments).getId();
    return messageService.getMessageById(messageId)
        .map(response -> ResponseEntity.status(201).body(response))
        .orElse(ResponseEntity.badRequest().build());
  }

  @Operation(summary = "메시지 수정")
  @ApiResponse(
      responseCode = "200",
      description = "메시지 수정 성공",
      content = @Content(mediaType = "*/*")
  )
  @PatchMapping("/{messageId}")
  public ResponseEntity<Void> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody UpdateMessageRequest request
  ) {
    if (!messageId.equals(request.messageId())) {
      return ResponseEntity.badRequest().build();
    }
    messageService.updateMessage(request);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "메시지 삭제")
  @DeleteMapping("/{messageId}")
  @ApiResponse(responseCode = "204", description = "메시지 삭제 성공")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteMessage(messageId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "채널 메시지 목록 조회")
  @GetMapping
  public ResponseEntity<List<MessageResponse>> getMessagesByChannel(@RequestParam UUID channelId) {
    return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
  }
}
