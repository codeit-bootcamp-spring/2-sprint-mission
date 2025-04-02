package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
public class MessageController {

  private final MessageService messageService;

  // 메시지 생성
  @Operation(summary = "Message 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = MessageResponse.class))),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{channelId}|{authorId}에 해당하는 Channel|Author를 찾을 수 없음")
          })),
  })
  @RequestMapping(value = "channels/{channelId}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponse> create(
      @PathVariable @Parameter(description = "Message를 생성할 채널") UUID channelId,
      @RequestPart("content") @Parameter(description = "Message 생성 정보") MessageCreateRequest request,
      @RequestPart(value = "file", required = false) @Parameter(description = "Message 첨부 파일들") List<MultipartFile> files) {
    List<BinaryContentCreateRequest> binaryContentList = new ArrayList<>();
    if (files != null) {
      for (MultipartFile file : files) {
        binaryContentList.add(BinaryContentCreateRequest.of(file));
      }
    }
    Message message = messageService.create(channelId, request, binaryContentList);
    return ResponseEntity.ok(MessageResponse.of(message));
  }

  // 메시지 수정
  @Operation(summary = "Message 내용 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = MessageResponse.class))),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{messageId}에 해당하는 Message를 찾을 수 없음")
          }))
  })
  @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
  public ResponseEntity<MessageResponse> update(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request) {
    Message message = messageService.update(messageId, request);
    return ResponseEntity.ok(MessageResponse.of(message));
  }

  // 메시지 삭제
  @Operation(summary = "Message 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = MessageResponse.class))),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{messageId}에 해당하는 Message를 찾을 수 없음")
          }))
  })
  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete(
      @PathVariable @Parameter(description = "삭제할 Message ID") UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  // 특정 채널의 메시지 목록 조회
  @Operation(summary = "Channel의 Message 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공"),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{channelId}에 해당하는 Channel을 찾을 수 없음")
          }))
  })
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<MessageResponse>> findAllByChannelId(
      @RequestParam @Parameter(description = "조회할 Channel ID") UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    List<MessageResponse> response = messages.stream().map(MessageResponse::of).toList();
    return ResponseEntity.ok(response);
  }
}
