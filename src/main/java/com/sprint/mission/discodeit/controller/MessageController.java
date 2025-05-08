package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> create(
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

        MessageDto message = messageService.create(request, attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Override
    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageDto> updateMessage(
            @PathVariable("messageId") UUID messageId,
            @RequestBody MessageUpdateRequest request) {
        MessageDto updatedMessage = messageService.updateMessage(messageId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
    }

    @Override
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable("messageId") UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
            @RequestParam("channelId") UUID channelId,
            @RequestParam(value = "cursor", required = false) Instant cursor,
            @PageableDefault(
                    size = 50,
                    page = 0,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }


}
