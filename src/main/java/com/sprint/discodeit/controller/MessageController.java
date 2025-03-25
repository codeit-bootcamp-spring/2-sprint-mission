package com.sprint.discodeit.controller;

import com.sprint.discodeit.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.domain.entity.Message;
import com.sprint.discodeit.service.basic.message.BasicMessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final BasicMessageService messageService;

    @PostMapping("/channels/{channelId}")
    public ResponseEntity<Message> sendMessage(@PathVariable String channelId,
                                               @RequestBody MessageRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.create(UUID.fromString(channelId), requestDto));
    }

    // [2] 메시지 수정
    @PutMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable String messageId,
                                                            @RequestBody MessageUpdateRequestDto updateDto) {
        return ResponseEntity.ok(messageService.update(UUID.fromString(messageId), updateDto));
    }

    // [3] 메시지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String messageId) {
        messageService.delete(UUID.fromString(messageId));
        return ResponseEntity.noContent().build();
    }

    // [4] 채널의 메시지 목록 조회
    @GetMapping("/channels/{channelId}")
    public ResponseEntity<List<Message>> getMessagesByChannel(@PathVariable String channelId) {
        return ResponseEntity.ok(messageService.findChannel(UUID.fromString(channelId)));
    }
}
