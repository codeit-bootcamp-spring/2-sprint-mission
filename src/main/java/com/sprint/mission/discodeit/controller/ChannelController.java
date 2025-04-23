package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.dto.service.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.service.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.service.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.service.channel.PublicChannelRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  // 공개 채널 생성
  @Override
  @PostMapping("/public")
  public ResponseEntity<ChannelDto> createPublic(
      @RequestBody @Valid PublicChannelRequest request) {
    ChannelDto response = channelService.create(request);
    return ResponseEntity.ok(response);
  }

  // 비공개 채널 생성
  @Override
  @PostMapping("/private")
  public ResponseEntity<ChannelDto> createPrivate(
      @RequestBody @Valid PrivateChannelRequest request) {
    ChannelDto response = channelService.create(request);
    return ResponseEntity.ok(response);
  }

  // 채널 삭제
  @Override
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  // 공개 채널 정보 수정
  @Override
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDto> updatePublic(@PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest request) {
    ChannelDto response = channelService.update(channelId, request);
    return ResponseEntity.ok(response);
  }

  // 특정 사용자가 볼 수 있는 모든 채널 목록을 조회
  @Override
  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAllByUserId(@RequestParam UUID userId) {
    List<ChannelDto> response = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(response);
  }


}