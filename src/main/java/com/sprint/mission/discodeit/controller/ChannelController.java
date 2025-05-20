package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {
  private final ChannelService channelService;
  private final ChannelMapper channelMapper;

  @PostMapping(path = "public")
  public ResponseEntity<ChannelDto> create(@Valid @RequestBody PublicChannelCreateRequest request) {
    log.info("▶▶ [API] Creating public channel - name: {}", request.name());
    Channel createdChannel = channelService.create(request);
    log.info("◀◀ [API] Public channel created - id: {}", createdChannel.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(channelMapper.toDto(createdChannel));
  }

  @PostMapping(path = "private")
  public ResponseEntity<ChannelDto> create(@Valid @RequestBody PrivateChannelCreateRequest request) {
    log.info("▶▶ [API] Creating private channel with {} participants", request.participantIds().size());
    Channel createdChannel = channelService.create(request);
    log.info("◀◀ [API] Private channel created - id: {}", createdChannel.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(channelMapper.toDto(createdChannel));
  }

  @PatchMapping(path = "{channelId}")
  public ResponseEntity<ChannelDto> update(@PathVariable("channelId") UUID channelId,
                                           @Valid @RequestBody PublicChannelUpdateRequest request) {
    log.info("▶▶ [API] Updating channel - id: {}", channelId);
    Channel updatedChannel = channelService.update(channelId, request);
    log.info("◀◀ [API] Channel updated - id: {}", channelId);
    return ResponseEntity.status(HttpStatus.OK).body(channelMapper.toDto(updatedChannel));
  }

  @DeleteMapping(path = "{channelId}")
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
    log.info("▶▶ [API] Deleting channel - id: {}", channelId);
    channelService.delete(channelId);
    log.info("◀◀ [API] Channel deleted - id: {}", channelId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    log.info("▶▶ [API] Finding channels for user - userId: {}", userId);
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    log.info("◀◀ [API] Found {} channels for user - userId: {}", channels.size(), userId);
    return ResponseEntity.status(HttpStatus.OK).body(channels);
  }
}