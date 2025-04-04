package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.request.MessageDeletRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageFinBychannelIDResponse;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> messageCreate(@RequestBody @Validated MessageCreateRequest messageCreateDto) {
        messageService.create(messageCreateDto);
        return ResponseEntity.ok(new MessageResponse(true, "메시지 생성 성공"));
    }

    @PatchMapping
    public ResponseEntity<MessageResponse> messageUpdate(@RequestBody @Validated MessageUpdateRequest messageUpdateDto) {
        messageService.update(messageUpdateDto);
        return ResponseEntity.ok(new MessageResponse(true, "메시지 수정 성공"));
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> messageDelete(@RequestBody @Validated MessageDeletRequest messageDeletDto) {
        messageService.delete(messageDeletDto.messageId());
        return ResponseEntity.ok(new MessageResponse(true, "메시지 삭제 성공"));
    }

    @GetMapping("/{channelID}")
    public ResponseEntity<List<Message>> messageFindByChannelId(@PathVariable("channelID") UUID channelID) {
        return ResponseEntity.ok(messageService.findByChannelId(channelID));
    }


}
