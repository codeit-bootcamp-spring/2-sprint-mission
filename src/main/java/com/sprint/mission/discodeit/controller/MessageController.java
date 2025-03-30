package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> send(
            @ModelAttribute SaveMessageRequestDto saveMessageRequestDto,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        messageService.sendMessage(saveMessageRequestDto, SaveBinaryContentRequestDto.nullableFromList(files));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable UUID messageId,
            @ModelAttribute UpdateMessageRequestDto updateMessageRequestDto,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        messageService.updateMessage(messageId, updateMessageRequestDto, SaveBinaryContentRequestDto.nullableFromList(files));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID messageId
    ) {
        messageService.deleteMessageById(messageId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<ApiResponse<List<FindMessageByChannelIdResponseDto>>> FindChannelMessage(
            @PathVariable UUID channelId
    ) {
        List<FindMessageByChannelIdResponseDto> findMessageByChannelIdDtoList = messageService.findMessageByChannelId(channelId);
        return ResponseEntity.ok(ApiResponse.success(findMessageByChannelIdDtoList));
    }
}
