package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> create(@RequestBody MessageCreateRequest request) {
        Message message = messageService.create(request, List.of());
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/{mesageId}", method = RequestMethod.PUT)
    public ResponseEntity<Message> update(@PathVariable UUID mesageId, @RequestBody MessageUpdateRequest request) {
        Message updated = messageService.update (mesageId, request);
        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value ="/channel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> findAllByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }
}
