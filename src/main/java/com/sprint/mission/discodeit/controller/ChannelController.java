package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "채널 API", description = "채널 관련 기능 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  @Operation(summary = "공개 채널 생성", description = "새로운 공개 채널을 생성합니다.")
  @PostMapping("/public")
  public ResponseEntity<ChannelDto> createPublicChannel(
      @RequestBody PublicChannelCreateRequest request) {
    return ResponseEntity.ok(channelService.find(channelService.create(request).getId()));
  }

  @Operation(summary = "비공개 채널 생성", description = "새로운 비공개 채널을 생성합니다.")
  @PostMapping("/private")
  public ResponseEntity<ChannelDto> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest request) {
    return ResponseEntity.ok(channelService.find(channelService.create(request).getId()));
  }

  @Operation(summary = "채널 수정", description = "특정 채널의 정보를 수정합니다.")
  @PutMapping("/public/{channelId}")
  public ResponseEntity<ChannelDto> updatePublicChannel(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    return ResponseEntity.ok(
        channelService.find(channelService.update(channelId, request).getId()));
  }

  @Operation(summary = "채널 삭제", description = "채널 ID를 이용하여 특정 채널을 삭제합니다.")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "사용자의 채널 목록 조회", description = "주어진 사용자 ID를 기반으로 사용자가 속한 채널 목록을 반환합니다.")
  @GetMapping
  public ResponseEntity<List<ChannelDto>> getUserChannels(@RequestParam UUID userId) {
    return ResponseEntity.ok(channelService.findAllByUserId(userId));
  }
}