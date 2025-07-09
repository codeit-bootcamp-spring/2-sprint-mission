package com.sprint.mission.discodeit.domain.channel.controller;

import com.sprint.mission.discodeit.domain.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.service.ChannelResult;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/public")
  public ResponseEntity<ChannelResult> createPublic(
      @Valid @RequestBody PublicChannelCreateRequest channelRegisterRequest) {
    ChannelResult aPublic = channelService.createPublic(channelRegisterRequest);

    return ResponseEntity.ok(aPublic);
  }

  @PostMapping("/private")
  public ResponseEntity<ChannelResult> createPrivate(
      @Valid @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
    ChannelResult aPrivate = channelService.createPrivate(privateChannelCreateRequest);

    return ResponseEntity.ok(aPrivate);
  }

  @GetMapping("/{channelId}")
  public ResponseEntity<ChannelResult> getById(@PathVariable UUID channelId) {
    ChannelResult channel = channelService.getById(channelId);

    return ResponseEntity.ok(channel);
  }

  @GetMapping
  public ResponseEntity<List<ChannelResult>> getAllByUserId(
      @RequestParam(value = "userId") UUID userId) {
    List<ChannelResult> channels = channelService.getAllByUserId(userId);

    return ResponseEntity.ok(channels);
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelResult> updatePublic(@PathVariable UUID channelId,
      @Valid @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
    ChannelResult channelResult = channelService.updatePublic(channelId,
        publicChannelUpdateRequest);

    return ResponseEntity.ok(channelResult);
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);

    return ResponseEntity.noContent().build();
  }

}

