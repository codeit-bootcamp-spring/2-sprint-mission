package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.dto.ReadStatusResponse;
import com.sprint.mission.discodeit.controller.dto.UserReadStatusResponse;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface MessageReadApi {

  // 특정 채널의 메시지 수신 정보 생성
  @Operation(summary = "Message 읽음 상태 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ReadStatusResponse.class))),
      @ApiResponse(responseCode = "400", description = "이미 읽음 상태가 존재함",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{userId}와 {channelId}를 사용하는 ReadStatus가 이미 존재함")
          })),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{userId}|{channelId}에 해당하는 User|Channel을 찾을 수 없음")
          }))
  })
  ResponseEntity<ReadStatusResponse> createByChannelId(ReadStatusCreateRequest request);

  // 특정 채널의 메시지 수신 정보를 수정
  @Operation(summary = "Message 읽음 상태 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ReadStatusResponse.class))),
      @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음")
  })
  ResponseEntity<ReadStatusResponse> updateByChannelId(
      @Parameter(description = "수정할 읽음 상태 ID") UUID id, ReadStatusUpdateRequest request);

  // 특정 사용자의 메시지 수신 정보를 조회
  @Operation(summary = "User의 Message 읽음 상태 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공",
          content = @Content(mediaType = "*/*", array = @ArraySchema(
              schema = @Schema(implementation = UserReadStatusResponse.class))))
  })
  ResponseEntity<List<UserReadStatusResponse>> findAllByUserId(
      @Parameter(description = "조회할 User ID") UUID userId);
}