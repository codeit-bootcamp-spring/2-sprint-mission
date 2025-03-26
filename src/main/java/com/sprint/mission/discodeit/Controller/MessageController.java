package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping(value ="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> sendMessage(@RequestPart("message") MessageCreateRequest messageCreateRequest,
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
        return ResponseEntity.ok(createdMessage);

    }

    @PutMapping(value = "/{messageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> updateMessage(@PathVariable("messageId") UUID messageId, @RequestBody MessageUpdateRequest updateRequest) {
        Message updatedMessage = messageService.update(messageId, updateRequest);
        return ResponseEntity.ok(updatedMessage);
    }


}
