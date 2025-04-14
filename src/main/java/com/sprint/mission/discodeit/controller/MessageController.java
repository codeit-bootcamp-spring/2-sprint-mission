package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.message.*;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.swagger.MessageApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController implements MessageApi {

  private final MessageService messageService;
  private final MessageMapper messageMapper;

  @Override
  @PostMapping
  public ResponseEntity<CreateMessageResponseDTO> createMessage(
      @RequestPart("messageCreateRequest") @Valid CreateMessageRequestDTO createMessageRequestDTO,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> multipartFileList) {
    MessageDTO messageDTO = messageService.create(
        messageMapper.toMessageParam(createMessageRequestDTO), multipartFileList);
    CreateMessageResponseDTO createdMessage = messageMapper.toMessageResponseDTO(messageDTO);
    return ResponseEntity.ok(createdMessage);
  }

  @Override
  @PatchMapping("/{messageId}")
  public ResponseEntity<UpdateMessageResponseDTO> updateMessage(@PathVariable("messageId") UUID id,
      @RequestPart("messageUpdateRequest") @Valid UpdateMessageRequestDTO updateMessageRequestDTO,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> multipartFileList) {

    UpdateMessageDTO updateMessageDTO = messageService.update(id,
        messageMapper.toUpdateMessageParam(updateMessageRequestDTO), multipartFileList);
    UpdateMessageResponseDTO updatedMessage = messageMapper.toUpdateMessageResponseDTO(
        updateMessageDTO);
    return ResponseEntity.ok(updatedMessage);
  }

  @Override
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable("messageId") UUID id) {
    messageService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  @GetMapping
  public ResponseEntity<List<MessageDTO>> getChannelMessages(@RequestParam("channelId") UUID id) {
    List<MessageDTO> messages = messageService.findAllByChannelId(id);
    return ResponseEntity.ok(messages);
  }
}
