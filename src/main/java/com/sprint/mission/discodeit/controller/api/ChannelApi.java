package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.dto.ChannelResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelByUserIdResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

@Tag(name = "Channel", description = "Channel API")
public interface ChannelApi {

  // 공개 채널 생성
  @Operation(summary = "Public Channel 생성")
  @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨",
      content = @Content(mediaType = "*/*", schema = @Schema(implementation = ChannelResponse.class)))
  ResponseEntity<ChannelResponse> createPublic(@Valid PublicChannelRequest request);

  // 비공개 채널 생성
  @Operation(summary = "Private Channel 생성")
  @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨",
      content = @Content(mediaType = "*/*", schema = @Schema(implementation = ChannelResponse.class)))
  ResponseEntity<ChannelResponse> createPrivate(@Valid PrivateChannelRequest request);

  // 채널 삭제
  @Operation(summary = "Channel 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{channelId}에 해당하는 Channel을 찾을 수 없음")
          }))
  })
  ResponseEntity<Void> delete(@Parameter(description = "삭제할 Channel ID") UUID channelId);

  // 공개 채널 정보 수정
  @Operation(summary = "Channel 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = ChannelResponse.class))),
      @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "비공개 채널은 수정 불가능")
          })),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{channelId}에 해당하는 Channel을 찾을 수 없음")
          }))
  })
  ResponseEntity<ChannelResponse> updatePublic(
      @Parameter(description = "수정할 Channel ID") UUID channelId, ChannelUpdateRequest request);

  // 특정 사용자가 볼 수 있는 모든 채널 목록을 조회
  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공",
          content = @Content(mediaType = "*/*", array =
          @ArraySchema(schema = @Schema(implementation = ChannelByUserIdResponse.class)))),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{userId}에 해당하는 User를 찾을 수 없음")
          }))
  })
  ResponseEntity<List<ChannelByUserIdResponse>> findAllByUserId(
      @Parameter(description = "조회할 User ID") UUID userId);
}
