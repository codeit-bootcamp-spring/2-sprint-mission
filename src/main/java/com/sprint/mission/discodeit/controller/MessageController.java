package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final FileMessageRepository fileMessageRepository;

    @GetMapping("/{channelId}")
    public ResponseEntity<List<MessageResponseDto>> getAllByChannelId(
        @PathVariable UUID channelId
    ) {
        List<MessageResponseDto> messageList = messageService.findByChannelId(channelId);

        return ResponseEntity.ok(messageList);
    }

    @PostMapping
    public ResponseEntity<MessageResponseDto> create(
        @RequestPart("message") CreateMessageRequest request,
        @RequestPart(value = "attachedImage", required = false) List<MultipartFile> files)
        throws IOException {

        UUID messageId;
        if (files == null || files.isEmpty()) {
            messageId = messageService.create(request);
        } else {
            List<CreateBinaryContentRequest> binaryContentRequests = convertFiles(files);
            messageId = messageService.create(request, binaryContentRequests);
        }

        MessageResponseDto response = messageService.findById(messageId);

        return ResponseEntity.ok(response);
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
            throw new RuntimeException("[ERROR] 파일 처리 중 오류 발생");
        }
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponseDto> update(
        @PathVariable UUID messageId,
        @RequestBody UpdateMessageRequest request) {

        messageService.update(messageId, request);
        MessageResponseDto response = messageService.findById(messageId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        messageService.remove(messageId);

        return ResponseEntity.noContent().build();
    }
}
