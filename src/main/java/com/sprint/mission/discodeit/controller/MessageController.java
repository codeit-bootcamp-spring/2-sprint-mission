package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;
  private final JwtUtil jwtUtil;

  @RequiresAuth
  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<?> createMessage(
      @RequestBody CreateMessageRequest request) {
    messageService.createMessage(request);

    return ResponseEntity.ok("메세지가 성공적으로 생성되었습니다.");
  }

  @RequiresAuth
  @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody UpdateMessageRequest request,
      HttpServletRequest httpRequest) {
    UUID userId = (UUID) httpRequest.getAttribute("user_id");

    messageService.updateMessage(userId, messageId, request);

    return ResponseEntity.ok("메세지가 업데이트 되었습니다.");
  }

  @RequiresAuth
  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteMessage(
      @PathVariable UUID messageId,
      HttpServletRequest httpRequest) {
    UUID userId = (UUID) httpRequest.getAttribute("user_id");
    messageService.deleteMessage(userId, messageId);

    return ResponseEntity.ok("메세지가 삭제 되었습니다.");
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<?> getChannelMessage(
      @RequestParam UUID channelId) {
    List<Message> messages = messageService.findallByChannelId(channelId);

    return ResponseEntity.ok(messages);
  }
}
