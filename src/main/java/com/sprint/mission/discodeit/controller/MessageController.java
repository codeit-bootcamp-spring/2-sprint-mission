package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "메시지 생성")
    @ApiResponse(responseCode = "201", description = "메시지 생성 성공")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> createMessage(
        @RequestPart("messageCreateRequest") CreateMessageRequest request,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        log.info("메시지 생성 API 호출 - authorId: {}, channelId: {}", request.authorId(),
            request.channelId());

        UUID messageId = messageService.createMessage(request, attachments).getId();
        MessageDto response = messageService.getMessageById(messageId);

        log.info("메시지 생성 완료 - messageId: {}", messageId);
        return ResponseEntity.status(201).body(response);
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
        log.info("메시지 수정 API 호출 - pathId: {}, requestId: {}", messageId, request.messageId());

        if (!messageId.equals(request.messageId())) {
            log.warn("메시지 ID 불일치 - pathId: {}, requestId: {}", messageId, request.messageId());
            return ResponseEntity.badRequest().build();
        }

        messageService.updateMessage(request);
        log.info("메시지 수정 완료 - messageId: {}", messageId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메시지 삭제")
    @DeleteMapping("/{messageId}")
    @ApiResponse(responseCode = "204", description = "메시지 삭제 성공")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        log.info("메시지 삭제 API 호출 - messageId: {}", messageId);
        messageService.deleteMessage(messageId);
        log.info("메시지 삭제 완료 - messageId: {}", messageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "채널 메시지 목록 조회")
    @ApiResponse(
        responseCode = "200",
        description = "채널 메시지 목록 조회 성공",
        content = @Content(mediaType = "*/*")
    )
    @GetMapping
    public ResponseEntity<PageResponse<MessageDto>> getMessagesByChannel(
        @RequestParam UUID channelId,
        @RequestParam(required = false) Instant cursor,
        @RequestParam(defaultValue = "50") int size
    ) {
        log.info("채널 메시지 조회 API 호출 - channelId: {}, cursor: {}, size: {}", channelId, cursor, size);
        PageResponse<MessageDto> response = messageService.findAllByChannelId(channelId, cursor,
            size);
        return ResponseEntity.ok(response);
    }
}
