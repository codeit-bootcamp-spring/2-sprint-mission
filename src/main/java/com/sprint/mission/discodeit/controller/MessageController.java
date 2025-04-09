package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/api/messages")
    public ResponseEntity<List<MessageResponseDto>> findAllByChannelId(
        @RequestParam UUID channelId
    ) {
        List<MessageResponseDto> messageList = messageService.findByChannelId(channelId);

        return ResponseEntity.ok(messageList);
    }

    @PostMapping("/api/messages")
    public ResponseEntity<MessageResponseDto> create(
        @RequestPart("messageCreateRequest") CreateMessageRequest request,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> files) {

        UUID messageId;
        if (files == null || files.isEmpty()) {
            messageId = messageService.create(request);
        } else {
            List<CreateBinaryContentRequest> binaryContentRequests = convertFiles(files);
            messageId = messageService.create(request, binaryContentRequests);
        }

        MessageResponseDto response = messageService.findById(messageId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private List<CreateBinaryContentRequest> convertFiles(List<MultipartFile> files) {

        return files.stream().map(this::convertFileToRequest).toList();
    }

    private CreateBinaryContentRequest convertFileToRequest(MultipartFile file) {
        try {
            return new CreateBinaryContentRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] binary content error");
        }
    }

    @PatchMapping("/api/messages/{messageId}")
    public ResponseEntity<MessageResponseDto> updateMessage(
        @PathVariable UUID messageId,
        @RequestBody UpdateMessageRequest request) {

        messageService.update(messageId, request);
        MessageResponseDto response = messageService.findById(messageId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.remove(messageId);

        return ResponseEntity.noContent().build();
    }
}
