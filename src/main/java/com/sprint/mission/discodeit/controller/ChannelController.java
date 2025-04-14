package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelInfoDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
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
  public ResponseEntity<Channel> createPrivateChannel(
      @RequestBody CreatePrivateChannelRequest channelDto
  ) {
    Channel channel = channelService.createPrivateChannel(channelDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }

  @RequestMapping(value = "/public", method = RequestMethod.POST)
  public ResponseEntity<Channel> createPublicChannel(
      @RequestBody CreatePublicChannelRequest channelDto) {
    Channel channel = channelService.createPublicChannel(channelDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
  public ResponseEntity<Channel> updateChannel(@PathVariable("channelId") UUID channelId,
      @RequestBody UpdateChannelRequest channelDto) {
    Channel channel = channelService.updateChannel(channelId, channelDto);

    return ResponseEntity.ok(channel);
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteChannel(@PathVariable("channelId") UUID channelId) {
    channelService.deleteChannel(channelId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Channel이 성공적으로 삭제됨");
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<ChannelInfoDto>> getAllChannels(
      @RequestParam(name = "userId") UUID userId) {
    List<ChannelInfoDto> channels = channelService.findAllByUserId(userId);

    return ResponseEntity.ok(channels);
  }
}
