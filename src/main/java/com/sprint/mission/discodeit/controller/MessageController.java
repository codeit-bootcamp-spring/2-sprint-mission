package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.message.*;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageResult;
import com.sprint.mission.discodeit.dto.service.message.FindMessageResult;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageResult;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.swagger.MessageApi;
import jakarta.validation.Valid;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Slf4j
public class MessageController implements MessageApi {

  private final MessageService messageService;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  @Override
  @PostMapping
  public ResponseEntity<CreateMessageResponseDTO> createMessage(
      @RequestPart("messageCreateRequest") @Valid CreateMessageRequestDTO createMessageRequestDTO,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> multipartFileList) {
    log.info("Message create request (authorId: {}, channelId: {}, fileCount: {})",
        createMessageRequestDTO.authorId(), createMessageRequestDTO.channelId(),
        multipartFileList.size());
    CreateMessageResult createMessageResult = messageService.create(
        messageMapper.toCreateMessageCommand(createMessageRequestDTO), multipartFileList);
    CreateMessageResponseDTO createdMessage = messageMapper.toCreateMessageResponseDTO(
        createMessageResult);
    return ResponseEntity.ok(createdMessage);
  }

  @Override
  @PatchMapping("/{messageId}")
  public ResponseEntity<UpdateMessageResponseDTO> updateMessage(@PathVariable("messageId") UUID id,
      @RequestPart("messageUpdateRequest") @Valid UpdateMessageRequestDTO updateMessageRequestDTO,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> multipartFileList) {
    log.info("Message update request (messageId: {}, fileCount: {})", id, multipartFileList.size());
    UpdateMessageResult updateMessageResult = messageService.update(id,
        messageMapper.toUpdateMessageCommand(updateMessageRequestDTO), multipartFileList);
    UpdateMessageResponseDTO updatedMessage = messageMapper.toUpdateMessageResponseDTO(
        updateMessageResult);
    return ResponseEntity.ok(updatedMessage);
  }

  @Override
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable("messageId") UUID id) {
    log.info("Message delete request (messageId: {})", id);
    messageService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Override
  @GetMapping
  public ResponseEntity<PageResponse<FindMessageResponseDTO>> getChannelMessages(
      @RequestParam("channelId") UUID id,
      @RequestParam(value = "cursor", required = false) Instant cursor,
      // 클라이언트 측에서는 이전 응답의 nextCursor값을 cursor로 넘겨준다.
      @RequestParam(value = "limit", defaultValue = "10") int limit) {
    Slice<FindMessageResult> messageResults;

    // 첫 페이징이라면 cursor가 존재하지 않으므로, cursor를 기준이 아닌 첫 페이지를 반환
    if (cursor == null) {
      messageResults = messageService.findAllByChannelIdInitial(id, limit);
    } else {
      messageResults = messageService.findAllByChannelIdAfterCursor(id, cursor, limit);
    }
    Slice<FindMessageResponseDTO> messageResponseDTOPage = messageResults.map(
        messageMapper::toFindMessageResponseDTO);

    return ResponseEntity.ok(pageResponseMapper.fromSlice(messageResponseDTOPage));
  }
}
