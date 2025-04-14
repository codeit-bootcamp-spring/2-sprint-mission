package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.Api.MessageApi;
import com.sprint.mission.discodeit.dto.request.Pageable;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
//@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

  private final MessageService messageService;
  private final ChannelService channelService;
  private final UserService userService;
  private final BinaryContentService binaryContentService;

  @Override
  public ResponseEntity<MessageDto> create2(
      MessageCreateRequest messageCreateRequest
      , List<MultipartFile> attachments) {
    if (messageCreateRequest == null) {
      return ResponseEntity.badRequest().build();
    }
    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
            .toList())
        .orElse(new ArrayList<>());

    Message message = messageService.create(messageCreateRequest, attachmentRequests);

    UserDto author = userService.find(message.getAuthorId());

    List<BinaryContentDto> binaryContentDtos = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              return new BinaryContentDto(
                  UUID.randomUUID(),
                  file.getName(),
                  file.getSize(),
                  file.getContentType()
              );
            })
            .toList())
        .orElse(new ArrayList<>());

    MessageDto dto = new MessageDto(message.getId(), message.getCreatedAt(), message.getUpdatedAt(),
        message.getContent(), message.getGetChannelId(), author, binaryContentDtos);

    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @Override
  public ResponseEntity<Void> delete1(Object messageId) {
    UUID uuid = UUID.fromString(String.valueOf(messageId));
    messageService.delete(uuid);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<PageResponse> findAllByChannelId(UUID ChannelId,
      Pageable pageable) {
    UUID uuid = UUID.fromString(String.valueOf(ChannelId));
    List<Message> messages = messageService.findAllBygetChannelId(uuid);

    PageResponse<Message> pageResponse = new PageResponse<Message>(
        messages,        // 실제 데이터 리스트
        pageable.page(),            // 현재 페이지 번호
        pageable.size(),               // 페이지 당 항목 수
        hashNext(pageable),        // 다음 페이지 존재 여부
        messages.size()
    );

    return ResponseEntity.ok(pageResponse);
  }

  @Override
  public ResponseEntity<MessageDto> update2(Object messageId,
      MessageUpdateRequest messageUpdateRequest) {
    UUID uuid = UUID.fromString(String.valueOf(messageId));
    Message updated = messageService.update(uuid,
        messageUpdateRequest);

    UserDto author = userService.find(updated.getAuthorId());

    List<BinaryContent> attachments =
        binaryContentService.findAllByIdIn(updated.getAttachmentIds());

    List<BinaryContentDto> binaryContentDtos = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              return new BinaryContentDto(
                  file.getId(),
                  file.getFileName(),
                  file.getSize(),
                  file.getContentType()
              );
            })
            .toList())
        .orElse(new ArrayList<>());

    MessageDto dto = new MessageDto(
        updated.getAuthorId(), updated.getCreatedAt(), updated.getUpdatedAt(),
        updated.getContent(), updated.getGetChannelId(), author, binaryContentDtos
    );

    return ResponseEntity.ok(dto);
  }

  private Boolean hashNext(Pageable pageable) {
    if (pageable.size() == pageable.page()) {
      return false;
    } else {
      return true;
    }
  }

}


/*
  @RequestMapping(
      path = "create",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
            .toList())
        .orElse(new ArrayList<>());
    Message createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdMessage);
  }

  @RequestMapping(path = "update")
  public ResponseEntity<Message> update(@RequestParam("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest request) {
    Message updatedMessage = messageService.update(messageId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedMessage);
  }

  @RequestMapping(path = "delete")
  public ResponseEntity<Void> delete(@RequestParam("messageId") UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @RequestMapping("findAllBygetChannelId")
  public ResponseEntity<List<Message>> findAllBygetChannelId(
      @RequestParam("getChannelId") UUID getChannelId) {
    List<Message> messages = messageService.findAllBygetChannelId(getChannelId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }
  */