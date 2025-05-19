package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  @RequestMapping(value = "/private", method = RequestMethod.POST)
  public ResponseEntity<ChannelDto> createPrivateChannel(
      @RequestBody CreatePrivateChannelRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPrivateChannel(request));
  }

  @RequestMapping(value = "/public", method = RequestMethod.POST)
  public ResponseEntity<ChannelDto> createPublicChannel(
      @RequestBody CreatePublicChannelRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPublicChannel(request));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
  public ResponseEntity<ChannelDto> updateChannel(@PathVariable("channelId") UUID channelId,
      @RequestBody UpdateChannelRequest request) {
    return ResponseEntity.ok(channelService.updateChannel(channelId, request));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteChannel(@PathVariable("channelId") UUID channelId) {
    channelService.deleteChannel(channelId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Channel이 성공적으로 삭제됨");
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<ChannelDto>> getAllChannels(
      @RequestParam(name = "userId") UUID userId) {
    return ResponseEntity.ok(channelService.findAllByUserId(userId));
  }
}
