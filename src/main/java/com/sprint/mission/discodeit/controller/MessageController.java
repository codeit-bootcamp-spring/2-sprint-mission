package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/messages")
public class MessageController {

  private final MessageService messageService;

  @RequiresAuth
  @PostMapping
  public ResponseEntity<MessageDto.Response> sendMessage(
      @RequestBody MessageDto.Create createMessage, HttpServletRequest httpRequest)
      throws IOException {
    String authorId = (String) httpRequest.getAttribute("userId");
    return ResponseEntity.status(HttpStatus.CREATED).body(
        messageService.create(createMessage, UUID.fromString(authorId)));
  }

  @RequiresAuth
  @PutMapping("/{messageId}")
  public ResponseEntity<MessageDto.Response> updateMessage(
      @Valid @PathVariable UUID messageId,
      @Valid @RequestBody MessageDto.Update updateMessage,
      HttpServletRequest httpRequest) throws IOException {
    String authorId = (String) httpRequest.getAttribute("userId");
    MessageDto.Response updatedMessage = messageService.updateMessage(messageId, updateMessage,
        UUID.fromString(authorId));
    return ResponseEntity.ok(updatedMessage);
  }

  @RequiresAuth
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@Valid @PathVariable UUID messageId) {
    boolean deleted = messageService.deleteMessage(messageId);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<MessageDto.Response>> getChannelMessages(
      @Valid @RequestParam("channelId") UUID channelId) {

    List<MessageDto.Response> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(messages);
  }

  @GetMapping("/{messageId}")
  public ResponseEntity<MessageDto.Response> getMessage(@PathVariable UUID messageId) {

    MessageDto.Response message = messageService.findByMessage(messageId);
    return ResponseEntity.ok(message);
  }
}
