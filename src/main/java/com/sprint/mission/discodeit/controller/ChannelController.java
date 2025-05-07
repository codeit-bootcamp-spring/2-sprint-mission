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
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<ChannelDto> create(
      @RequestBody PublicChannelCreateRequest request) {
    ChannelDto publicChannel = channelService.create(request);
    log.info("{}", LogMapUtil.of("action", "createPublic")
        .add("publicChannel", publicChannel));

    return ResponseEntity.status(HttpStatus.CREATED).body(publicChannel);
  }

  @PostMapping("/private")
  public ResponseEntity<ChannelDto> create(
      @RequestBody PrivateChannelCreateRequest request) {
    ChannelDto privateChannel = channelService.create(request);
    log.info("{}", LogMapUtil.of("action", "createPrivate")
        .add("privateChannel", privateChannel));

    return ResponseEntity.status(HttpStatus.CREATED).body(privateChannel);
  }

  @PutMapping("/{channelId}")
  public ResponseEntity<ChannelDto> update(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    ChannelDto updated = channelService.update(channelId, request);
    log.info("{}", LogMapUtil.of("action", "updatePublic")
        .add("updated", updated));

    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Channel> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    log.info("{}", LogMapUtil.of("action", "delete")
        .add("request", channelId));

    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> readAll(@RequestParam UUID userId) {
    List<ChannelDto> channelDtoList = channelService.readAllByUserId(userId);
    log.info("{}", LogMapUtil.of("action", "readAll")
        .add("channelDtoList", channelDtoList));

    return ResponseEntity.ok(channelDtoList);
  }
}
