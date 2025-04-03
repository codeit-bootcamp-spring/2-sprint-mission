package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelInfoDto;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  @RequestMapping(value = "/private", method = RequestMethod.POST)
  public ResponseEntity<?> createPrivateChannel(
      @RequestBody CreatePrivateChannelRequest channelDto) {
    Channel channel = channelService.createPrivateChannel(channelDto);

    return ResponseEntity.ok(channel.getId());
  }

  @RequestMapping(value = "/public", method = RequestMethod.POST)
  public ResponseEntity<?> createPublicChannel(@RequestBody CreatePublicChannelRequest channelDto) {
    Channel channel = channelService.createPublicChannel(channelDto);

    return ResponseEntity.ok("Public 채널이 생성되었습니다.");
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateChannel(@PathVariable("channelId") UUID channelId,
      @RequestBody UpdateChannelRequest channelDto) {
    channelService.updateChannel(channelId, channelDto);

    return ResponseEntity.ok("채널 수정이 완료되었습니다.");
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteChannel(@PathVariable("channelId") UUID channelId) {
    channelService.deleteChannel(channelId);

    return ResponseEntity.ok("채널이 삭제되었습니다.");
  }

  @RequiresAuth
  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<?> getAllChannels(HttpServletRequest httpRequest) {
    UUID userId = (UUID) httpRequest.getAttribute("userId");

    List<ChannelInfoDto> channels = channelService.findAllByUserId(userId);

    return ResponseEntity.ok(channels);
  }
}
