package com.sprint.mission.discodeit.core.message.controller;

import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.message.controller.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.core.message.controller.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.message.controller.dto.MessageDeleteResponse;
import com.sprint.mission.discodeit.core.message.controller.dto.PageResponse;
import com.sprint.mission.discodeit.core.message.usecase.MessageService;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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

@Tag(name = "Message", description = "메시지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> create(
      @RequestPart("messageCreateRequest") @Valid MessageCreateRequest requestBody,
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

    CreateMessageCommand command = MessageDtoMapper.toCreateMessageCommand(requestBody);

    MessageDto result = messageService.create(command, attachmentRequests);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(result);
  }

  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findAll(@RequestParam UUID channelId,
      @RequestParam(required = false) Instant cursor,
      @PageableDefault(
          size = 50, sort = "createdAt", direction = Direction.DESC
      ) Pageable pageable) {
    Slice<MessageDto> results = messageService.findAllByChannelId(channelId, cursor,
        pageable);
    PageResponse<MessageDto> pageResponse = PageResponseMapper.fromSlice(results,
        MessageDto::createdAt);
    return ResponseEntity.ok(pageResponse);
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest requestBody) {
    UpdateMessageCommand command = MessageDtoMapper.toUpdateMessageCommand(messageId, requestBody);
    MessageDto result = messageService.update(command);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<MessageDeleteResponse> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.ok(new MessageDeleteResponse(true));
  }

}
