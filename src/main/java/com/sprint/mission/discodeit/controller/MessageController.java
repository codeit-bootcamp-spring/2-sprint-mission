package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final BasicMessageService messageService;

    @PostMapping
    public ResponseEntity<Message> create(@RequestBody MessageCreateRequestDto dto) {
        Message message = messageService.create(dto);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> find(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.find(id));
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Message>> findAllByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    @PatchMapping
    public ResponseEntity<Message> update(@RequestBody MessageUpdateRequestDto dto) {
        return ResponseEntity.ok(messageService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}