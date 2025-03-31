package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.MessageResponseDTO;
import com.sprint.mission.discodeit.dto.UpdateMessageDTO;
import com.sprint.mission.discodeit.service.MessageService;
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

    // post 메시지 생성
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageResponseDTO> createMessage(@RequestBody CreateMessageDTO createMessageDTO){
        MessageResponseDTO created = messageService.create(createMessageDTO);
        return ResponseEntity.ok(created);
    }

    // PUT 메시지 수정
    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateMessage(@RequestBody UpdateMessageDTO updateMessageDTO){
        messageService.update(updateMessageDTO);
        return ResponseEntity.ok().build();
    }

    // DELETE 메시지 삭제
    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable("messageId") String messageId){
        messageService.delete(UUID.fromString(messageId));
        return ResponseEntity.ok().build();
    }

    // GET 특정 채널의 메시지 조회
    @RequestMapping(value = "/cahnnel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByChannel(
            @PathVariable("channelId") String channelId){
        List<MessageResponseDTO> messages = messageService.findAllByChannelId(UUID.fromString(channelId));
        return ResponseEntity.ok(messages);
    }

}
