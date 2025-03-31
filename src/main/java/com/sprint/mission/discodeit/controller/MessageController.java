package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages/")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(value = "/send", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<UUID> create(@RequestPart("message") MessageCreateRequest messageCreateRequest, @RequestPart(value = "attachments", required = false) List<MultipartFile> attachmentFiles) {
        List<BinaryContentCreateRequest> binaryContentCreateRequests = Optional.ofNullable(attachmentFiles)
                .orElse(List.of())
                .stream()
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
                .collect(Collectors.toList());

        Message message = messageService.create(messageCreateRequest, binaryContentCreateRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(message.getId());
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Message> update(@PathVariable UUID id, @RequestBody MessageUpdateRequest messageUpdateRequest) {
        Message message = messageService.update(id, messageUpdateRequest);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<Message> get(@PathVariable UUID id) {
        Message message = messageService.find(id);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/getAll/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getAll(@PathVariable UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }
}
