package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final BasicMessageService messageService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Message> createMessage(
            @RequestBody MessageCreateRequest messageCreateRequest,
            @RequestParam Optional<List<BinaryContentCreateRequest>> binaryContentCreateRequests) {

        Message createdMessage = messageService.create(messageCreateRequest, binaryContentCreateRequests.orElse(List.of()));

        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{messageId}", method = RequestMethod.PUT)
    public ResponseEntity<Message> updateMessage(
            @PathVariable UUID messageId,
            @RequestBody MessageUpdateRequest messageUpdateRequest) {

        Message updatedMessage = messageService.update(messageId, messageUpdateRequest);

        return new ResponseEntity<>(updatedMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(
            @PathVariable UUID messageId) {

        messageService.delete(messageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/channels/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessagesByChannel(
            @PathVariable UUID channelId) {

        List<Message> messages = messageService.findAllByChannelId(channelId);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
