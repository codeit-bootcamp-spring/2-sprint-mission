package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.Message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.Message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;
  private final BinaryContentService binaryContentService;

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<Message> createMessage(
      @RequestPart("messageCreateRequest") CreateMessageRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    Message message = messageService.createMessage(request);
    if (!attachments.isEmpty()) {
      attachments.stream()
          .map(binaryContentService::createBinaryContent)
          .forEach(binaryId -> messageService.addAttachment(message.getId(), binaryId));
    }

    return ResponseEntity.status(201).body(message);
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
  public ResponseEntity<Message> updateMessage(
      @PathVariable("messageId") UUID messageId,
      @RequestBody UpdateMessageRequest request) {
    Message message = messageService.updateMessage(messageId, request);

    return ResponseEntity.ok(message);
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteMessage(
      @PathVariable("messageId") UUID messageId) {
    messageService.deleteMessage(messageId);

    return ResponseEntity.status(204).body("Message가 성공적으로 삭제됨");
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<Message>> getChannelMessage(
      @RequestParam("channelId") UUID channelId) {
    List<Message> messages = messageService.findallByChannelId(channelId);

    return ResponseEntity.ok(messages);
  }
}
