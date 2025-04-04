package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.LogMapUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel Api")
public class ChannelController {

  private final ChannelService channelService;
  private static final Logger log = LoggerFactory.getLogger(ChannelController.class);

  @PostMapping("/public")
  public ResponseEntity<Channel> create(
      @RequestBody PublicChannelCreateRequest request) {
    Channel publicChannel = channelService.create(request);
    log.info("{}", LogMapUtil.of("action", "createPublic")
        .add("publicChannel", publicChannel));

    return ResponseEntity.ok(publicChannel);
  }

  @PostMapping("/private")
  public ResponseEntity<Channel> create(
      @RequestBody PrivateChannelCreateRequest request) {
    Channel privateChannel = channelService.create(request);
    log.info("{}", LogMapUtil.of("action", "createPrivate")
        .add("privateChannel", privateChannel));

    return ResponseEntity.ok(privateChannel);
  }

  @PutMapping("/{channelKey}")
  public ResponseEntity<Channel> update(@PathVariable UUID channelKey,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel updated = channelService.update(channelKey, request);
    log.info("{}", LogMapUtil.of("action", "updatePublic")
        .add("updated", updated));

    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{channelKey}")
  public ResponseEntity<Channel> delete(@PathVariable UUID channelKey) {
    channelService.delete(channelKey);
    log.info("{}", LogMapUtil.of("action", "delete")
        .add("request", channelKey));

    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> readAll(@RequestParam UUID userKey) {
    List<ChannelDto> channelDtoList = channelService.readAllByUserKey(userKey);
    log.info("{}", LogMapUtil.of("action", "readAll")
        .add("channelDtoList", channelDtoList));

    return ResponseEntity.ok(channelDtoList);
  }
}
