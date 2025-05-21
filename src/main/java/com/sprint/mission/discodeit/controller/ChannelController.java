package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.channel.*;
import com.sprint.mission.discodeit.dto.service.channel.CreatePrivateChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.CreatePublicChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.FindChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelResult;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.swagger.ChannelApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;
  private final ChannelMapper channelMapper;

  @Override
  @PostMapping("/public")
  public ResponseEntity<CreatePublicChannelResponseDTO> createPublicChannel(
      @RequestBody @Valid CreatePublicChannelRequestDTO createChannelRequestDTO) {
    log.info("Public channel create request (channelName: {})", createChannelRequestDTO.name());
    CreatePublicChannelResult createPublicChannelResult =
        channelService.createPublicChannel(
            channelMapper.toCreatePublicChannelCommand(createChannelRequestDTO));
    CreatePublicChannelResponseDTO createdChannel = channelMapper.toCreatePublicChannelResponseDTO(
        createPublicChannelResult);
    return ResponseEntity.ok(createdChannel);
  }

  @Override
  @PostMapping("/private")
  public ResponseEntity<CreatePrivateChannelResponseDTO> createPrivateChannel(
      @RequestBody @Valid CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO) {
    log.info("Private channel create request (participants: {})",
        createPrivateChannelRequestDTO.participantIds());
    CreatePrivateChannelResult createPrivateChannelResult =
        channelService.createPrivateChannel(
            channelMapper.toCreatePrivateChannelCommand(createPrivateChannelRequestDTO));
    CreatePrivateChannelResponseDTO createdChannel = channelMapper.toCreatePrivateChannelResponseDTO(
        createPrivateChannelResult);
    return ResponseEntity.ok(createdChannel);
  }

  @Override
  @PatchMapping("/{channelId}")
  public ResponseEntity<UpdateChannelResponseDTO> updateChannel(
      @PathVariable("channelId") UUID channelId,
      @RequestBody @Valid UpdateChannelRequestDTO updateChannelRequestDTO) {
    log.info("channel update request (channelId: {})", channelId);
    UpdateChannelResult updateChannelResult =
        channelService.update(channelId,
            channelMapper.toUpdateChannelCommand(updateChannelRequestDTO));
    UpdateChannelResponseDTO updatedChannel = channelMapper.toUpdateChannelResponseDTO(
        updateChannelResult);
    return ResponseEntity.ok(updatedChannel);
  }

  @Override
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable("channelId") UUID channelId) {
    log.info("channel delete request (channelId: {})", channelId);
    channelService.delete(channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  @GetMapping
  public ResponseEntity<List<FindChannelResponseDTO>> findAll(@RequestParam("userId") UUID userId) {
    List<FindChannelResult> findChannelResults = channelService.findAllByUserId(userId);
    List<FindChannelResponseDTO> channels = findChannelResults.stream()
        .map(channelMapper::toFindChannelResponseDTO)
        .toList();
    return ResponseEntity.ok(channels);
  }
}


