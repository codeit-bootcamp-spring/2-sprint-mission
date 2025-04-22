package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping(value ="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> sendMessage(@RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
                                               @RequestPart(name="attachments", required = false) MultipartFile[] attachmentFiles) {
        List<BinaryContentCreateRequest> attachments = new ArrayList<>();
        if (attachmentFiles != null) {
            for (MultipartFile file : attachmentFiles) {
                if (!file.isEmpty()) {
                    try {
                        BinaryContentCreateRequest attachment = BinaryContentCreateRequest.builder()
                                .fileName(file.getOriginalFilename())
                                .contentType(file.getContentType())
                                .bytes(file.getBytes())
                                .build();
                        attachments.add(attachment);
                    }catch(IOException e) {
                        throw new RuntimeException("Error creating attachment", e);
                    }
                }
            }
        }
        Message createdMessage = messageService.create(messageCreateRequest, attachments);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    @PatchMapping(value = "/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable("messageId") UUID messageId, @RequestBody MessageUpdateRequest updateRequest) {
        Message updatedMessage = messageService.update(messageId, updateRequest);
        return ResponseEntity.ok(updatedMessage);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Message> deleteMessage(@PathVariable("messageId") UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity <PageResponse<MessageDto>> getAllMessageChannel(
            @RequestParam UUID channelId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
            ){
        Slice<MessageDto> slice = messageService.findAllByChannelId(channelId, pageable);
        PageResponse<MessageDto> response = PageResponseMapper.fromSlice(slice);

        return ResponseEntity.ok(response);
    }

}
