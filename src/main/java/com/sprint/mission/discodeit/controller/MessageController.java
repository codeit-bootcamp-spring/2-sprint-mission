package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final JwtUtil jwtUtil;

    @RequiresAuth
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createMessage(
            @RequestBody CreateMessageRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = UUID.fromString(jwtUtil.extractUserId(token));

        messageService.createMessage(userId, request);

        return ResponseEntity.ok("메세지가 성공적으로 생성되었습니다.");
    }

    @RequiresAuth
    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMessage(
            @PathVariable UUID messageId,
            @RequestBody UpdateMessageRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = UUID.fromString(jwtUtil.extractUserId(token));

        messageService.updateMessage(userId, messageId, request);

        return ResponseEntity.ok("메세지가 업데이트 되었습니다.");
    }

    @RequiresAuth
    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMessage(
            @PathVariable UUID messageId,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = UUID.fromString(jwtUtil.extractUserId(token));
        messageService.deleteMessage(userId, messageId);

        return ResponseEntity.ok("메세지가 삭제 되었습니다.");
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<?> getChannelMessage(
            @PathVariable UUID channelId) {
        List<Message> messages = messageService.findallByChannelId(channelId);

        return ResponseEntity.ok(messages);
    }
}
