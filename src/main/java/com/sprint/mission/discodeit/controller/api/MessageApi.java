package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.controller.MessageDto;
import com.sprint.mission.discodeit.dto.service.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
public interface MessageApi {

  // 메시지 생성
  @Operation(summary = "Message 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = MessageDto.class))),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{channelId}|{authorId}에 해당하는 Channel|Author를 찾을 수 없음")
          })),
  })
  ResponseEntity<MessageDto> create(
      @Parameter(description = "Message 생성 정보") @Valid MessageCreateRequest request,
      @Parameter(description = "Message 첨부 파일들") List<MultipartFile> files);

  // 메시지 수정
  @Operation(summary = "Message 내용 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = MessageDto.class))),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{messageId}에 해당하는 Message를 찾을 수 없음")
          }))
  })
  ResponseEntity<MessageDto> update(UUID messageId, MessageUpdateRequest request);

  // 메시지 삭제
  @Operation(summary = "Message 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{messageId}에 해당하는 Message를 찾을 수 없음")
          }))
  })
  ResponseEntity<Void> delete(@Parameter(description = "삭제할 Message ID") UUID messageId);

  // 특정 채널의 메시지 목록 조회
  /*
  @Operation(summary = "Channel의 Message 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공",
          content = @Content(mediaType = "*"/*", array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class)))), // mediaType = "*"/*"-> " 빼기!!!!!!!!!
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(mediaType = "*"/*", examples = {
              @ExampleObject(value = "{channelId}에 해당하는 Channel을 찾을 수 없음")
          }))
  })
  ResponseEntity<List<MessageResponse>> findAllByChannelId(
      @Parameter(description = "조회할 Channel ID") UUID channelId);
   */
}