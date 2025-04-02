package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

  private final ChannelService channelService;

  @Operation(summary = "Public Channel 생성")
  @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨", content = @Content(mediaType = "*/*", schema = @Schema(implementation = Channel.class)))
  @PostMapping("/public")
  private ResponseEntity<ChannelDto> create(
      @RequestBody PublicChannelCreateRequest publicChannelCreateRequest) {
    Channel createPublicChannel = channelService.create(publicChannelCreateRequest);
    ChannelDto channelDto = channelService.find(createPublicChannel.getId());

    return new ResponseEntity<>(channelDto, HttpStatus.CREATED);
  }

  @Operation(summary = "Private Channel 생성")
  @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨", content = @Content(mediaType = "*/*", schema = @Schema(implementation = Channel.class)))
  @PostMapping("/Private")
  private ResponseEntity<ChannelDto> create(
      @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
    Channel createPrivateChannel = channelService.create(privateChannelCreateRequest);
    ChannelDto channelDto = channelService.find(createPrivateChannel.getId());

    return new ResponseEntity<>(channelDto, HttpStatus.CREATED);
  }

  @Operation(summary = "Channel 정보 수정")
  @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음", content = @Content(mediaType = "*/*", examples = {
      @ExampleObject(value = "Channel with id {channelId} not found")}))
  @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음", content = @Content(mediaType = "*/*", examples = {
      @ExampleObject(value = "Private channel cannot be updated")}))
  @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨", content = @Content(mediaType = "*/*", schema = @Schema(implementation = Channel.class)))
  @PutMapping("/{channelId}")
  private ResponseEntity<ChannelDto> update(
      @Parameter(description = "수정할 Channel ID") @PathVariable("channelId") UUID channelId,
      @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {

    Channel updatePublicChannel = channelService.update(channelId, publicChannelUpdateRequest);
    ChannelDto channelDto = channelService.find(updatePublicChannel.getId());

    return new ResponseEntity<>(channelDto, HttpStatus.OK);
  }

  @Operation(summary = "Channel 삭제")
  @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음", content = @Content(mediaType = "*/*", examples = {
      @ExampleObject(value = "Channel with id {channelId} not found")}))
  @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨", content = @Content)
  @DeleteMapping("/{channelId}")
  private ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 Channel ID") @PathVariable("channelId") UUID channelId) {

    channelService.delete(channelId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공", content = @Content(mediaType = "*/*", array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class))))
  @GetMapping
  private ResponseEntity<List<ChannelDto>> findAll(
      @Parameter(description = "조회할 User ID") @RequestParam("userId") UUID userId) {

    List<ChannelDto> channels = channelService.findAllByUserId(userId);

    return new ResponseEntity<>(channels, HttpStatus.OK);
  }
}
