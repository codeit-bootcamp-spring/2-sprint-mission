package com.sprint.mission.discodeit.adapter.inbound.message;

import static com.sprint.mission.discodeit.adapter.inbound.message.MessageDtoMapper.toCreateMessageCommand;
import static com.sprint.mission.discodeit.adapter.inbound.message.MessageDtoMapper.toUpdateMessageCommand;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageCreateResponse;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageDeleteResponse;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageUpdateResponse;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.UpdateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.crud.MessageService;
import com.sprint.mission.discodeit.core.message.usecase.crud.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.crud.dto.MessageListResult;
import com.sprint.mission.discodeit.core.message.usecase.crud.dto.MessageResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/channels/{channelId}/messages")
public class MessageController {

  private final MessageService messageService;

  //TODO. List<Optional>구조 마음에 안들어서 개편할 예정
  @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageCreateResponse> create(
      @PathVariable UUID userId, @PathVariable UUID channelId,
      @RequestPart("message") MessageCreateRequest requestBody,
      @RequestPart(value = "profileImage", required = false) List<MultipartFile> files)
      throws IOException {

    List<Optional<BinaryContentCreateRequestDTO>> list = new ArrayList<>();
    if (files != null) {
      for (MultipartFile file : files) {
        Optional<BinaryContentCreateRequestDTO> binaryContentRequest = Optional.empty();
        if (file != null && !file.isEmpty()) {
          binaryContentRequest = Optional.of(new BinaryContentCreateRequestDTO(
              file.getOriginalFilename(),
              file.getContentType(),
              file.getBytes()
          ));
          list.add(binaryContentRequest);
        }
      }
    }
    CreateMessageCommand command = toCreateMessageCommand(userId, channelId,
        requestBody);
    messageService.create(command, list);
    return ResponseEntity.ok(new MessageCreateResponse(true, "Message Create Successfully"));
  }

  @GetMapping
  public ResponseEntity<List<MessageResult>> findAll(@PathVariable UUID channelId) {
    MessageListResult result = messageService.findMessagesByChannelId(channelId);

    //TODO. 25.04.02 MessageController findAll, 더 좋은 API 생각이 떠오르지 않아서 임시로 List<MessageResult> 반환
    return ResponseEntity.ok(result.messageList());
  }

  @PutMapping("/{messageId}")
  public ResponseEntity<MessageUpdateResponse> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest requestBody) {
    UpdateMessageCommand command = toUpdateMessageCommand(messageId, requestBody);
    messageService.update(command);
    return ResponseEntity.ok(new MessageUpdateResponse(true));
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<MessageDeleteResponse> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.ok(new MessageDeleteResponse(true));
  }

}
