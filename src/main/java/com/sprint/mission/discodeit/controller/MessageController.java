package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> createMessage(@RequestPart MessageCreateRequest messageCreateRequest
    , @RequestPart(required = false) List<MultipartFile> attachmentFiles) throws IOException {
        List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();

        if (attachmentFiles != null) {
            for (MultipartFile file : attachmentFiles) {
                String fileName = file.getOriginalFilename();
                String contentType = file.getContentType();
                byte[] bytes = file.getBytes();
                BinaryContentCreateRequest binaryRequest = new BinaryContentCreateRequest(fileName, contentType, bytes);
                binaryContentCreateRequests.add(binaryRequest);
            }
        }

        Message message = messageService.create(messageCreateRequest, binaryContentCreateRequests);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId,
                                                 @RequestBody MessageUpdateRequest request) {
        Message updatedMessage = messageService.update(messageId, request);
        return ResponseEntity.ok(updatedMessage);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessagesByChannel(@PathVariable UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }
}
