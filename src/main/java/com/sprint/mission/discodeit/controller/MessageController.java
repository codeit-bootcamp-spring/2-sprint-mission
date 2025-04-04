package com.sprint.mission.discodeit.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.sprint.mission.discodeit.dto.ApiDataResponse;
import com.sprint.mission.discodeit.dto.FindMessageByChannelIdResponseDto;
import com.sprint.mission.discodeit.dto.SaveBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Message", description = "Message API")
public class MessageController {

  private final MessageService messageService;

  @PostMapping(path = "", consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Message 생성", operationId = "create_2")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = Message.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject("Channel | Author with id {channelId | authorId} not found")
          )
      )
  })
  public ResponseEntity<ApiDataResponse<Void>> send(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> files
  ) throws IOException {
    messageService.sendMessage(messageCreateRequest,
        SaveBinaryContentRequestDto.nullableFromList(files));
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiDataResponse.success());
  }

  @PutMapping("/{messageId}")
  @Operation(summary = "Message 내용 수정", operationId = "update_2")
  @Parameters(value = {
      @Parameter(
          name = "messageId",
          in = ParameterIn.PATH,
          description = "수정할 Message ID",
          required = true,
          schema = @Schema(type = "string", format = "uuid")
      )
  })
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = MessageUpdateRequest.class)
      ),
      required = true
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "메세지가 성공적으로 수정됨",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = Message.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "메세지를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject("Message with id {messageId} not found")
          )
      )
  })
  public ResponseEntity<ApiDataResponse<Void>> update(
      @PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  ) {
    messageService.updateMessage(messageId, messageUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(ApiDataResponse.success());
  }

  @DeleteMapping("/{messageId}")
  @Operation(summary = "Message 삭제", operationId = "delete_1")
  @Parameters({
      @Parameter(
          name = "messageId",
          in = ParameterIn.PATH,
          description = "삭제할 Mesaage ID",
          required = true,
          schema = @Schema(type = "string", format = "uuid")
      )
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "메세지가 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Message를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject("Message with id {messageId} not found")
          )
      )
  })
  public ResponseEntity<ApiDataResponse<Void>> delete(
      @PathVariable("messageId") UUID messageId
  ) {
    messageService.deleteMessageById(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiDataResponse.success());
  }

  @GetMapping("")
  @Operation(summary = "Channel의 Message 목록 조회")
  @Parameters(value = {
      @Parameter(
          name = "channelId",
          in = ParameterIn.QUERY,
          description = "조회할 Channel ID",
          required = true,
          schema = @Schema(type = "string", format = "uuid")
      )
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Message목록 조회 성공",
          content = @Content(
              mediaType = "*/*",
              array = @ArraySchema(schema = @Schema(implementation = Message.class))
          )
      )
  })
  public ResponseEntity<ApiDataResponse<List<FindMessageByChannelIdResponseDto>>> FindChannelMessage(
      @RequestParam("channelId") UUID channelId
  ) {
    List<FindMessageByChannelIdResponseDto> findMessageByChannelIdDtoList = messageService.findMessageByChannelId(
        channelId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiDataResponse.success(findMessageByChannelIdDtoList));
  }
}
