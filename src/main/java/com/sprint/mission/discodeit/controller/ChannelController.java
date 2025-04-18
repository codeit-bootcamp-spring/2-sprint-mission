package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @GetMapping
  public ResponseEntity<List<ChannelResponse>> findAllChannelByUser(
      @RequestParam UUID userId) {
    List<ChannelResponse> response = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/public")
  public ResponseEntity<ChannelResponse> createPublicChannel(
      @RequestBody PublicChannelCreateRequest request) {
    ChannelResponse response = channelService.createPublicChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/private")
  public ResponseEntity<ChannelResponse> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest request) {
    ChannelResponse response = channelService.createPrivateChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelResponse> update(
      @PathVariable("channelId") UUID channelId,
      @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
    ChannelResponse response = channelService.update(channelId, publicChannelUpdateRequest);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
