package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.Channel;
import com.sprint.mission.discodeit.controller.dto.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/channel")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  @Override
  public ResponseEntity<Channel> create3(PublicChannelCreateRequest publicChannelCreateRequest) {
    channelService.create(publicChannelCreateRequest);
    return ChannelApi.super.create3(publicChannelCreateRequest);
  }

  @Override
  public ResponseEntity<Channel> create4(PrivateChannelCreateRequest privateChannelCreateRequest) {
    channelService.create(privateChannelCreateRequest);
    return ChannelApi.super.create4(privateChannelCreateRequest);
  }

  @Override
  public ResponseEntity<Void> delete2(UUID getChannelId) {
    channelService.delete(getChannelId);
    return ChannelApi.super.delete2(getChannelId);
  }

  @Override
  public ResponseEntity<Channel> update3(UUID getChannelId,
      PublicChannelUpdateRequest publicChannelUpdateRequest) {
    channelService.update(getChannelId, publicChannelUpdateRequest);
    return ChannelApi.super.update3(getChannelId, publicChannelUpdateRequest);
  }

  @Override
  public ResponseEntity<List<ChannelDto>> findAll1(UUID userId) {
    channelService.findAllByUserId(userId);
    return ChannelApi.super.findAll1(userId);
  }
/*
  @RequestMapping(path = "createPublic")
  public ResponseEntity<Channel> create(@RequestBody PublicChannelCreateRequest request) {
    Channel createdChannel = channelService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  @RequestMapping(path = "createPrivate")
  public ResponseEntity<Channel> create(@RequestBody PrivateChannelCreateRequest request) {
    Channel createdChannel = channelService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  @RequestMapping(path = "update")
  public ResponseEntity<Channel> update(@RequestParam("getChannelId") UUID getChannelId,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel udpatedChannel = channelService.update(getChannelId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(udpatedChannel);
  }

  @RequestMapping(path = "delete")
  public ResponseEntity<Void> delete(@RequestParam("getChannelId") UUID getChannelId) {
    channelService.delete(getChannelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @RequestMapping(path = "findAll")
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }*/
}
