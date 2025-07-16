package com.sprint.mission.discodeit.core.channel.controller;

import com.sprint.mission.discodeit.core.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.core.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.service.ChannelSearchService;
import com.sprint.mission.discodeit.core.channel.service.ChannelService;
import com.sprint.mission.discodeit.swagger.ChannelApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Channel", description = "채널 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;
  private final ChannelSearchService channelSearchService;

  @PostMapping("/public")
  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  public ResponseEntity<ChannelDto> create(
      @RequestBody @Valid PublicChannelCreateRequest request) {
    ChannelDto result = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @PostMapping("/private")
  public ResponseEntity<ChannelDto> create(
      @RequestBody @Valid PrivateChannelCreateRequest request) {
    ChannelDto result = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelDto> result = channelSearchService.findAllByUserId(userId);
    return ResponseEntity.ok(result);
  }

  @PatchMapping("/{channelId}")
  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  public ResponseEntity<ChannelDto> update(@PathVariable UUID channelId,
      @RequestBody @Valid ChannelUpdateRequest request) {
    ChannelDto result = channelService.update(channelId, request);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{channelId}")
  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }
}
