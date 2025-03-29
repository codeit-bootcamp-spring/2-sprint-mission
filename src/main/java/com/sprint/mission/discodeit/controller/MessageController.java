package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @RequiresAuth
    @PostMapping("/create")
    public ResponseEntity<MessageDto.Response> sendMessage(
            @RequestBody MessageDto.Create createMessage) throws IOException {

        return ResponseEntity.ok(messageService.create(createMessage));
    }
    
    @RequiresAuth
    @PutMapping("/update/{messageId}")
    public ResponseEntity<MessageDto.Response> updateMessage(
            @PathVariable UUID messageId,
            @RequestBody MessageDto.Update updateMessage,
            HttpServletRequest httpRequest) throws IOException {

        String userId = (String) httpRequest.getAttribute("userId");
        UUID userUuid;

        updateMessage.setMessageId(messageId);
        
        MessageDto.Response updatedMessage = messageService.updateMessage(updateMessage);
        return ResponseEntity.ok(updatedMessage);
    }
    
    @RequiresAuth
    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {

        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<MessageDto.Response>> getChannelMessages(@PathVariable UUID channelId) {

        List<MessageDto.Response> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/{messageId}")
    public ResponseEntity<MessageDto.Response> getMessage(@PathVariable UUID messageId) {

        MessageDto.Response message = messageService.findByMessage(messageId);
        return ResponseEntity.ok(message);
    }
}
