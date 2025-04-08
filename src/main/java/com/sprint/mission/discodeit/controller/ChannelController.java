package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequestDto request) {
    Channel channel = channelService.createPublic(request);
    return ResponseEntity.ok(channel);
  }

  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivate(
      @RequestBody PrivateChannelCreateRequestDto request) {
    Channel channel = channelService.createPrivate(request);
    return ResponseEntity.ok(channel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ChannelResponseDto> find(@PathVariable("id") UUID channelId) {
    ChannelResponseDto channel = channelService.find(channelId);
    return ResponseEntity.ok(channel);
  }

  @GetMapping
  public ResponseEntity<List<ChannelResponseDto>> findAllByUser(
      @RequestParam("userId") UUID userId) {
    List<ChannelResponseDto> channels = channelService.findAllByUserID(userId);
    return ResponseEntity.ok(channels);
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<Channel> update(@PathVariable("channelId") UUID channelId,
      @RequestBody ChannelUpdateRequestDto request) {
    Channel updatedChannel = channelService.update(request);
    return ResponseEntity.ok(updatedChannel);
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }
}
