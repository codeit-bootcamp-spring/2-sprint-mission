package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Message")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
  public ResponseEntity<UUID> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachmentFiles) {
    List<BinaryContentCreateRequest> binaryContentCreateRequests = BinaryContentCreateRequest.of(
        attachmentFiles);
    Message message = messageService.create(messageCreateRequest, binaryContentCreateRequests);
    return ResponseEntity.status(HttpStatus.CREATED).body(message.getId());
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
  public ResponseEntity<Message> update(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {
    Message message = messageService.update(messageId, messageUpdateRequest);
    return ResponseEntity.ok(message);
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }
  
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Message>> getAll(@RequestParam UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(messages);
  }
}
