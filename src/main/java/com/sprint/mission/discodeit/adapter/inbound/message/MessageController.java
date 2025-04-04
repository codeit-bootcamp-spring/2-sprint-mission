package com.sprint.mission.discodeit.adapter.inbound.message;

import static com.sprint.mission.discodeit.adapter.inbound.message.MessageDtoMapper.toCreateMessageCommand;
import static com.sprint.mission.discodeit.adapter.inbound.message.MessageDtoMapper.toUpdateMessageCommand;

import com.sprint.mission.discodeit.adapter.inbound.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.message.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.message.response.MessageCreateResponse;
import com.sprint.mission.discodeit.adapter.inbound.message.response.MessageDeleteResponse;
import com.sprint.mission.discodeit.adapter.inbound.message.response.MessageUpdateResponse;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.message.usecase.MessageService;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageListResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageCreateResponse> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest requestBody,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments)
      throws IOException {

    List<CreateBinaryContentCommand> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return CreateBinaryContentCommand.create(file);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
            .toList())
        .orElse(new ArrayList<>());

    CreateMessageCommand command = toCreateMessageCommand(requestBody);

    CreateMessageResult result = messageService.create(command, attachmentRequests);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(MessageCreateResponse.create(result.message()));
  }

  @GetMapping
  public ResponseEntity<List<MessageResult>> findAll(@RequestParam UUID channelId) {
    MessageListResult result = messageService.findMessagesByChannelId(channelId);

    return ResponseEntity.ok(result.messageList());
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageUpdateResponse> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest requestBody) {
    UpdateMessageCommand command = toUpdateMessageCommand(messageId, requestBody);
    UpdateMessageResult result = messageService.update(command);
    return ResponseEntity.ok(MessageUpdateResponse.create(result.message()));
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<MessageDeleteResponse> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.ok(new MessageDeleteResponse(true));
  }

}
