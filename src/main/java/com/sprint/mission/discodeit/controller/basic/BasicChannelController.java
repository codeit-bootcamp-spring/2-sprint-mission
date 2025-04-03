package com.sprint.mission.discodeit.controller.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.basic.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Channel", description = "Channel API")
@RestController
@RequestMapping("api/channels")
@RequiredArgsConstructor
public class BasicChannelController {

  private final ChannelService channelService;
  private static final Logger logger = LoggerFactory.getLogger(BasicChannelController.class);

  @Operation(summary = "Public Channel 생성")
  @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨")
  @PostMapping("/public")
  public ResponseEntity<Channel> createPublic(
      @Parameter(description = "공개 채널 생성 요청 정보")
      @RequestBody PublicChannelCreateRequest request) {
    Channel response = channelService.createPublic(request);
    logger.info("Successfully create public channel: {}", response);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Private Channel 생성")
  @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨")
  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivate(
      @Parameter(description = "비공개 채널 생성 요청 정보")
      @RequestBody PrivateChannelCreateRequest request) {
    Channel response = channelService.createPrivate(request);
    logger.info("Successfully create private channel: {}", response);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Channel 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음"),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음")
  })
  @PatchMapping("/{channelId}")
  public ResponseEntity<Channel> updateChannel(
      @Parameter(description = "수정할 Channel ID")
      @PathVariable UUID channelId,
      @Parameter(description = "Channel 수정 요청 정보")
      @RequestBody PublicChannelUpdateRequest request) {
    Channel response = channelService.update(channelId, request);
    logger.info("Successfully update public channel: {}", request);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Channel 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음")
  })
  @DeleteMapping("/{channelId}")
  public ResponseEntity<String> deleteChannel(
      @Parameter(description = "삭제할 Channel ID")
      @PathVariable String channelId) {
    channelService.delete(UUID.fromString(channelId));
    logger.info("Successfully delete channel: {}", channelId);
    return ResponseEntity.ok("Successfully delete channel: " + channelId);
  }

  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공")
  @GetMapping
  public ResponseEntity<List<ChannelDto>> findChannelsByUserId(
      @Parameter(description = "조회할 User ID")
      @RequestParam UUID userId) {
    List<ChannelDto> response = channelService.findAllByUserId(userId);
    logger.info("Successfully find channels by userId: {}", userId);
    return ResponseEntity.ok(response);
  }
}
