package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/public")
  public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequest request) {
    Channel channel = channelService.createPublic(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channel);
  }

  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivate(
      @RequestBody PrivateChannelCreateRequest request) {
    Channel channel = channelService.createPrivate(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ChannelResponse> find(@PathVariable("id") UUID channelId) {
    ChannelResponse channel = channelService.find(channelId);
    return ResponseEntity.ok(channel);
  }

  @GetMapping
  public ResponseEntity<List<ChannelResponse>> findAllByUser(
      @RequestParam("userId") UUID userId) {
    List<ChannelResponse> channels = channelService.findAllByUserID(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<Channel> update(@PathVariable("channelId") UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel updatedChannel = channelService.update(channelId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedChannel);
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
