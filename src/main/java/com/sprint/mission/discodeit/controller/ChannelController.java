package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.channel.ChannelByUserIdResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

  private final ChannelService channelService;

  // 공개 채널 생성
  @Operation(summary = "Public Channel 생성")
  @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨",
      content = @Content(mediaType = "*/*", schema = @Schema(implementation = ChannelResponse.class)))
  @RequestMapping(value = "/public", method = RequestMethod.POST)
  public ResponseEntity<ChannelResponse> createPublic(@RequestBody PublicChannelRequest request) {
    Channel channel = channelService.create(request);
    return ResponseEntity.ok(ChannelResponse.of(channel));
  }

  // 비공개 채널 생성
  @Operation(summary = "Private Channel 생성")
  @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨",
      content = @Content(mediaType = "*/*", schema = @Schema(implementation = ChannelResponse.class)))
  @RequestMapping(value = "/private", method = RequestMethod.POST)
  public ResponseEntity<ChannelResponse> createPrivate(@RequestBody PrivateChannelRequest request) {
    Channel channel = channelService.create(request);
    return ResponseEntity.ok(ChannelResponse.of(channel));
  }

  // 채널 삭제
  @Operation(summary = "Channel 삭제")
  @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨")
  @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
      content = @Content(mediaType = "*/*", examples = {
          @ExampleObject(value = "{channelId}에 해당하는 Channel이 없음")
      }))
  @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  // 공개 채널 정보 수정
  @Operation(summary = "Channel 정보 수정")
  @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨",
      content = @Content(mediaType = "*/*", schema = @Schema(implementation = ChannelResponse.class)))
  @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음",
      content = @Content(mediaType = "*/*", examples = {
          @ExampleObject(value = "비공개 채널은 수정 불가능")
      }))
  @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
      content = @Content(mediaType = "*/*", examples = {
          @ExampleObject(value = "{channelId}에 해당하는 Channel이 없음")
      }))
  @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
  public ResponseEntity<ChannelResponse> updatePublic(@PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest request) {
    Channel channel = channelService.update(channelId, request);
    return ResponseEntity.ok(ChannelResponse.of(channel));
  }

  // 특정 사용자가 볼 수 있는 모든 채널 목록을 조회
  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공",
      content = @Content(mediaType = "*/*", schema = @Schema(implementation = ChannelByUserIdResponse.class)))
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ChannelByUserIdResponse>> findAllByUserId(@RequestParam UUID userId) {
    List<ChannelByUserIdResponse> response = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(response);
  }


}