package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @RequiresAuth
  @PostMapping("/public")
  public ResponseEntity<ChannelDto.Response> createPublicChannel(
      @Valid @RequestBody ChannelDto.CreatePublic channelDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPublicChannel(channelDto));
  }

  @RequiresAuth
  @PostMapping("/private")
  public ResponseEntity<ChannelDto.Response> createPrivateChannel(
      @Valid @RequestBody ChannelDto.CreatePrivate channelDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPrivateChannel(channelDto));
  }

  @RequiresAuth
  @PutMapping("/{channelId}")
  public ResponseEntity<ChannelDto.Response> updateChannel(
      @Valid @PathVariable UUID channelId,
      @Valid @RequestBody ChannelDto.Update channelDto) {

    return ResponseEntity.ok(channelService.updateChannel(channelDto, channelId));
  }

  @RequiresAuth
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @Valid @PathVariable UUID channelId, HttpServletRequest httpRequest) {
    String ownerId = (String) httpRequest.getAttribute("userId");
    channelService.deleteChannel(channelId, UUID.fromString(ownerId));

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

  }

  @GetMapping
  public ResponseEntity<List<ChannelDto.Response>> getChannelsForUser(
      @Valid @RequestParam("userId") UUID userId) {

    List<ChannelDto.Response> channels = channelService.getAccessibleChannels(userId);
    return ResponseEntity.ok(channels);
  }

  @GetMapping("/{channelId}")
  public ResponseEntity<ChannelDto.Response> getChannel(@Valid @PathVariable UUID channelId) {

    ChannelDto.Response channel = channelService.getChannelDetails(channelId);
    return ResponseEntity.ok(channel);
  }
}