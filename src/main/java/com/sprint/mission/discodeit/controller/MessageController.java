package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<Message>> create(@Valid @RequestBody MessageCreateRequest request) {
        Message message = messageService.create(request);
        ApiResponse<Message> apiResponse = new ApiResponse<>("메시지 전송 성공", message);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Message>> update(@Valid @RequestBody MessageUpdateRequest request) {
        Message updatedMessage = messageService.update(request);
        ApiResponse<Message> apiResponse = new ApiResponse<>("메시지 수정 성공", updatedMessage);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        ApiResponse<Void> apiResponse = new ApiResponse<>("메시지 삭제 성공", null);
        return ResponseEntity.ok(apiResponse);
    }
}
