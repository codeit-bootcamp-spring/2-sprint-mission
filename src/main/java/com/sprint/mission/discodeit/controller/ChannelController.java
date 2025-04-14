package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.channel.*;
import com.sprint.mission.discodeit.dto.service.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.FindChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelDTO;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.swagger.ChannelApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;
  private final ChannelMapper channelMapper;

  @Override
  @PostMapping("/public")
  public ResponseEntity<CreatePublicChannelResponseDTO> createPublicChannel(
      @RequestBody @Valid CreatePublicChannelRequestDTO createChannelRequestDTO) {
    ChannelDTO channelDTO =
        channelService.createPublicChannel(channelMapper.toChannelParam(createChannelRequestDTO));
    CreatePublicChannelResponseDTO createdChannel = channelMapper.toChannelResponseDTO(channelDTO);
    return ResponseEntity.ok(createdChannel);
  }

  @Override
  @PostMapping("/private")
  public ResponseEntity<CreatePrivateChannelResponseDTO> createPrivateChannel(
      @RequestBody @Valid CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO) {
    PrivateChannelDTO privateChannelDTO =
        channelService.createPrivateChannel(
            channelMapper.toPrivateChannelParam(createPrivateChannelRequestDTO));
    CreatePrivateChannelResponseDTO createdChannel = channelMapper.toPrivateChannelResponseDTO(
        privateChannelDTO);
    return ResponseEntity.ok(createdChannel);
  }

  @Override
  @PatchMapping("/{channelId}")
  public ResponseEntity<UpdateChannelResponseDTO> updateChannel(
      @PathVariable("channelId") UUID channelId,
      @RequestBody @Valid UpdateChannelRequestDTO updateChannelRequestDTO) {
    UpdateChannelDTO updateChannelDTO =
        channelService.update(channelId,
            channelMapper.toUpdateChannelParam(updateChannelRequestDTO));
    UpdateChannelResponseDTO updatedChannel = channelMapper.toUpdateChannelResponseDTO(
        updateChannelDTO);
    return ResponseEntity.ok(updatedChannel);
  }

  @Override
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable("channelId") UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  @GetMapping
  public ResponseEntity<List<FindChannelDTO>> findAll(@RequestParam("userId") UUID userId) {
    List<FindChannelDTO> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }
}


