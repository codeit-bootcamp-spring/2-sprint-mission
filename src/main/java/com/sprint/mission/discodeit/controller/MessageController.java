package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.Message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.Message.MessageDto;
import com.sprint.mission.discodeit.dto.Message.UpdateMessageRequest;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<MessageDto>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId) {
    return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
  }

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<MessageDto> createMessage(
      @RequestPart("messageCreateRequest") CreateMessageRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    MessageDto messageDto = messageService.createMessage(request);
    if (!attachments.isEmpty()) {
      attachments.stream()
          .map(binaryContentService::createBinaryContent)
          .forEach(binaryId -> messageService.addAttachment(messageDto.id(), binaryId));
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
  public ResponseEntity<MessageDto> updateMessage(
      @PathVariable("messageId") UUID messageId,
      @RequestBody UpdateMessageRequest request) {
    return ResponseEntity.ok(messageService.updateMessage(messageId, request));
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteMessage(
      @PathVariable("messageId") UUID messageId) {
    messageService.deleteMessage(messageId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Message가 성공적으로 삭제됨");
  }


}
