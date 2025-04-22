package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Channel")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

//  @PostMapping
//  public ResponseEntity<ChannelDto> createChannel(@RequestBody ChannelCreateRequest request) {
//    ChannelDto dto = channelService.createChannel(request);
//    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
//  }

  @RequestMapping(value = "/public", method = RequestMethod.POST)
  public ResponseEntity<ChannelDto> publicChannelCreate(
      @RequestBody PublicChannelCreateRequest request) {
    Channel channel = channelService.create(request);
    ChannelDto dto = channelService.find(channel.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @RequestMapping(value = "/private", method = RequestMethod.POST)
  public ResponseEntity<ChannelDto> privateChannelCreate(
      @RequestBody PrivateChannelCreateRequest request) {
    Channel channel = channelService.create(request);
    ChannelDto dto = channelService.find(channel.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
  public ResponseEntity<ChannelDto> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
  public ResponseEntity<ChannelDto> update(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel channel = channelService.update(channelId, request);
    ChannelDto channelDto = channelService.find(channel.getId());
    return ResponseEntity.status(HttpStatus.OK).body(channelDto);
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ChannelDto>> getAll(@RequestParam UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(channels);
  }
}
