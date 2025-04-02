package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.dto.message.*;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<MessageCreateResponse>> sendMessage(@Valid @RequestBody MessageCreateRequest request) {
        UUID messageId = messageService.createMessage(request);
        ApiResponse<MessageCreateResponse> response = new ApiResponse<>(true, "메세지 전송 성공", new MessageCreateResponse(messageId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageUpdateResponse>> updateMessage(@PathVariable UUID id, @Valid @RequestBody MessageUpdateRequest request) {
        messageService.updateMessage(id, request);
        ApiResponse<MessageUpdateResponse> response = new ApiResponse<>(true, "메세지 업데이트 성공", new MessageUpdateResponse(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageDeleteResponse>> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        ApiResponse<MessageDeleteResponse> response = new ApiResponse<>(true, "메세지 삭제 성공", new MessageDeleteResponse(id, Instant.now()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{channelId}/messages")
    public ResponseEntity<ApiResponse<List<MessageReadResponse>>> findMessageList(@PathVariable UUID channelId) {
        List<MessageReadResponse> readResponses=  messageService.findAllByChannelId(channelId);
        ApiResponse<List<MessageReadResponse>> response = new ApiResponse<>(true, "메세지 리스트 불러오기 성공", readResponses);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
