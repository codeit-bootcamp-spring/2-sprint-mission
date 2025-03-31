package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.MessageUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> create(@ModelAttribute MessageCreateDto createDto) {
        Message message = messageService.createMessage(createDto);
        MessageResponseDto response = MessageResponseDto.convertToResponseDto(message);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageResponseDto> update(@ModelAttribute MessageUpdateDto updateDto) {     //@PathVariable UUID id
        Message message = messageService.updateMessage(updateDto);
        MessageResponseDto response = MessageResponseDto.convertToResponseDto(message);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        MessageResponseDto messageResponseDto = messageService.findById(id);
        messageService.deleteMessage(id, messageResponseDto.userId(), messageResponseDto.channelId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<List<MessageResponseDto>> getAllByChannelId(@PathVariable UUID channelId) {
        List<MessageResponseDto> channelList = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(channelList);
    }

}
