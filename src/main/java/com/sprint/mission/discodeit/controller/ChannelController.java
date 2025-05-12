package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private static final Logger log = LoggerFactory.getLogger(ChannelController.class);

  private final ChannelService channelService;

  @GetMapping
  public ResponseEntity<List<ChannelResponse>> findAllChannelByUser(
      @RequestParam @NotNull(message = "사용자 ID는 필수입니다.") UUID userId) {
    log.debug("GET /api/channels - 사용자 채널 목록 조회 요청: userId={}", userId);

    List<ChannelResponse> response = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/public")
  public ResponseEntity<ChannelResponse> createPublicChannel(
      @Valid @RequestBody PublicChannelCreateRequest request) {
    log.info("POST /api/channels/public - 공개 채널 생성 요청: name={}", request.name());

    ChannelResponse response = channelService.createPublicChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/private")
  public ResponseEntity<ChannelResponse> createPrivateChannel(
      @Valid @RequestBody PrivateChannelCreateRequest request) {
    log.info("POST /api/channels/private - 비공개 채널 생성 요청: participantIds={}",
        request.participantIds());
    ChannelResponse response = channelService.createPrivateChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelResponse> update(
      @PathVariable("channelId") @NotNull(message = "채널 ID는 필수입니다.") UUID channelId,
      @Valid @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
    log.info("PATCH /api/channels/{} - 채널 수정 요청: newName={}, newDescription={}",
        channelId, publicChannelUpdateRequest.newName(),
        publicChannelUpdateRequest.newDescription());

    ChannelResponse response = channelService.update(channelId, publicChannelUpdateRequest);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(
      @PathVariable @NotNull(message = "채널 ID는 필수입니다.") UUID channelId) {
    log.info("DELETE /api/channels/{} - 채널 삭제 요청", channelId);

    channelService.delete(channelId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
