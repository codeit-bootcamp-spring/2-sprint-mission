package com.sprint.mission.discodeit.controller.basic;


import com.sprint.mission.discodeit.controller.BinaryContentRequestResolver;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Tag(name = "Message", description = "Message API")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class BasicMessageController {

  private final MessageService messageService;
  private final BinaryContentRequestResolver binaryContentRequestResolver;
  Logger logger = LoggerFactory.getLogger(BasicMessageController.class);

  @Operation(summary = "Message 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음"),
      @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨")
  })
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Message> create(
      @Parameter(description = "Message 생성 요청 정보")
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @Parameter(description = "Message 첨부 파일들(선택 사항)")
      @RequestPart(value = "attachments", required = false) Optional<List<MultipartFile>> attachments) {

    List<BinaryContentCreateRequest> binaryContentCreateRequests = attachments
        .map(contents -> contents.stream()
            .map(binaryContentRequestResolver::resolve)
            .flatMap(Optional::stream)
            .collect(Collectors.toList()))
        .orElse(List.of());

    Message response = messageService.create(request, binaryContentCreateRequests);
    logger.info("Successfully created message: {}", response);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(description = "Message가 성공적으로 수정됨")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음")
  })
  @PatchMapping("/{messageId}")
  public ResponseEntity<Message> update(
      @Parameter(description = "수정할 Message ID")
      @PathVariable String messageId,
      @Parameter(description = "Message 수정 요청 정보")
      @RequestBody MessageUpdateRequest request) {
    Message response = messageService.update(UUID.fromString(messageId), request);
    logger.info("Successfully updated message: {}", response);
    return ResponseEntity.ok(response);
  }

  @Operation(description = "삭제할 Message ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음")
  })
  @DeleteMapping("/{messageId}")
  public ResponseEntity<String> delete(
      @Parameter(description = "삭제할 Message ID")
      @PathVariable String messageId) {
    messageService.delete(UUID.fromString(messageId));
    logger.info("Successfully deleted message: {}", messageId);
    return ResponseEntity.ok("Successfully deleted message: " + messageId);
  }

  @Operation(summary = "Channel의 Message 목록 조회")
  @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공")
  @GetMapping
  public ResponseEntity<List<Message>> findAll(
      @Parameter(name = "channelId", in = ParameterIn.QUERY, description = "조회할 Channel Id", required = true,
          schema = @Schema(type = "string", format = "uuid"))
      @RequestParam String channelId) {
    List<Message> response = messageService.findAllByChannelId(UUID.fromString(channelId));
    logger.info("Successfully found all messages: {}", response);
    return ResponseEntity.ok(response);
  }
}
