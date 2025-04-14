package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.Api.ChannelApi;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
//@RequestMapping("/api/channel")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  @Override
  public ResponseEntity<ChannelDto> create3(PublicChannelCreateRequest publicChannelCreateRequest) {
    Channel created = channelService.create(publicChannelCreateRequest);

    ChannelDto dto = new ChannelDto(created.getId(), created.getType(),
        created.getName(), created.getDescription(), null, created.getCreatedAt());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(dto);
  }

  @Override
  public ResponseEntity<ChannelDto> create4(
      PrivateChannelCreateRequest privateChannelCreateRequest) {
    Channel created = channelService.create(privateChannelCreateRequest);
    ChannelDto dto = new ChannelDto(created.getId(), created.getType(),
        created.getName()
        , created.getDescription(), privateChannelCreateRequest.participantIds(),
        created.getCreatedAt());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(dto);
  }

  @Override
  public ResponseEntity<Void> delete2(Object ChannelId) {
    UUID uuid = UUID.fromString(ChannelId.toString());
    channelService.delete(uuid);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  public ResponseEntity<ChannelDto> update3(Object getChannelId,
      PublicChannelUpdateRequest publicChannelUpdateRequest) {
    UUID uuid = UUID.fromString(getChannelId.toString());
    Channel updated = channelService.update(uuid, publicChannelUpdateRequest);

    ChannelDto dto = new ChannelDto(updated.getId(), updated.getType(), updated.getName()
        , updated.getDescription(), null, updated.getCreatedAt()

    );

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(dto);
  }

  @Override
  public ResponseEntity<Object> findAll1(Object userId) {
    UUID uuid = UUID.fromString(userId.toString());
    channelService.findAllByUserId(uuid);
    return ChannelApi.super.findAll1(uuid);
  }
/*
  @RequestMapping(path = "createPublic")
  public ResponseEntity<Channel> create(@RequestBody PublicChannelCreateRequest request) {
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
