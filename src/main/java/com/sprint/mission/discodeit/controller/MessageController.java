package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.message.*;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Message-Controller", description = "Message 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;
  private final MessageMapper messageMapper;

  @Operation(summary = "메시지 생성",
      description = "userId와 channelId를 가지는 메시지를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "메시지 생성 성공"),
          @ApiResponse(responseCode = "404", description = "userId 또는 channelId에 해당하는 리소스가 존재하지 않음"),
      })
  @PostMapping
  public ResponseEntity<CreateMessageResponseDTO> createMessage(
      @RequestPart("messageCreateRequest") @Valid CreateMessageRequestDTO createMessageRequestDTO,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> multipartFileList) {
    MessageDTO messageDTO = messageService.create(
        messageMapper.toMessageParam(createMessageRequestDTO), multipartFileList);
    CreateMessageResponseDTO createdMessage = messageMapper.toMessageResponseDTO(messageDTO);
    return ResponseEntity.ok(createdMessage);
  }

  @Operation(summary = "메시지 수정",
      description = "messageId에 해당하는 메시지를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "메시지 수정 성공"),
          @ApiResponse(responseCode = "400", description = "필드가 올바르게 입력되지 않음 (DTO 유효성 검증 실패)"),
          @ApiResponse(responseCode = "404", description = "messageId에 해당하는 메시지가 존재하지 않음")
      })
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

  @Operation(summary = "메시지 삭제",
      description = "messageId로 메시지를 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "메시지 삭제 성공")
      })
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable("messageId") UUID id) {
    messageService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Operation(summary = "채널 내 메시지 조회",
      description = "channelId를 가진 메시지들을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "채널 내 메시지 조회 성공")
      })
  @GetMapping
  public ResponseEntity<List<MessageDTO>> getChannelMessages(@RequestParam("channelId") UUID id) {
    List<MessageDTO> messages = messageService.findAllByChannelId(id);
    return ResponseEntity.ok(messages);
  }
}
