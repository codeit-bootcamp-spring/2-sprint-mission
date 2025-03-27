package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<MessageCreateResponse>> sendMessage(@Valid @RequestBody MessageCreateRequest request) {
        UUID messageId = messageService.createMessage(request);
        ApiResponse<MessageCreateResponse> response = new ApiResponse<>(true, "메세지 전송 선공", new MessageCreateResponse(messageId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }


}
