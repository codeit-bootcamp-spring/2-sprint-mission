package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.controller.dto.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "Message", description = "Message API")
public class MessageController implements MessageApi {

  private final MessageService messageService;

  // 메시지 생성
  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponse> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> files) {
    List<BinaryContentCreateRequest> binaryContentList = new ArrayList<>();
    if (files != null) {
      for (MultipartFile file : files) {
        binaryContentList.add(BinaryContentCreateRequest.of(file));
      }
    }
    Message message = messageService.create(request, binaryContentList);
    return ResponseEntity.ok(MessageResponse.of(message));
  }

  // 메시지 수정
  @Override
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageResponse> update(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request) {
    Message message = messageService.update(messageId, request);
    return ResponseEntity.ok(MessageResponse.of(message));
  }

  // 메시지 삭제
  @Override
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  // 특정 채널의 메시지 목록 조회
  @Override
  @GetMapping
  public ResponseEntity<List<MessageResponse>> findAllByChannelId(@RequestParam UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    List<MessageResponse> response = messages.stream().map(MessageResponse::of).toList();
    return ResponseEntity.ok(response);
  }
}
