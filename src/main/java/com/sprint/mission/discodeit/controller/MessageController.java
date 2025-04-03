package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.controller.dto.ApiMessage;
import com.sprint.mission.discodeit.controller.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.controller.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

  private final MessageService messageService;

  @Override
  public ResponseEntity<ApiMessage> create2(MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
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

    ModelMapper modelMapper = new ModelMapper();
    ApiMessage apiMessage = modelMapper.map(message, ApiMessage.class);

    return ResponseEntity.status(HttpStatus.CREATED).body(apiMessage);
  }

  @Override
  public ResponseEntity<Void> delete1(UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<ApiMessage>> findAllBygetChannelId(UUID getChannelId) {
    List<Message> messages = messageService.findAllBygetChannelId(getChannelId);
    ModelMapper modelMapper = new ModelMapper();
    List<ApiMessage> apiMessages = new ArrayList<>();
    for (Message message : messages) {
      ApiMessage apiMessage = modelMapper.map(message, ApiMessage.class);
      apiMessages.add(apiMessage);
    }

    return ResponseEntity.ok(apiMessages);
  }

  @Override
  public ResponseEntity<ApiMessage> update2(UUID messageId,
      MessageUpdateRequest messageUpdateRequest) {
    Message updatedMessage = messageService.update(messageId,
        messageUpdateRequest);

    ModelMapper modelMapper = new ModelMapper();
    ApiMessage updateApiMessage = modelMapper.map(updatedMessage, ApiMessage.class);
    return ResponseEntity.ok(updateApiMessage);
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