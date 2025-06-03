package com.sprint.mission.discodeit.core.channel.controller;

import com.sprint.mission.discodeit.core.channel.controller.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.core.channel.controller.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.controller.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.controller.dto.ChannelDeleteResponse;
import com.sprint.mission.discodeit.core.channel.usecase.ChannelService;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PrivateChannelCreateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PublicChannelCreateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelUpdateCommand;
import com.sprint.mission.discodeit.swagger.ChannelApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


  @PostMapping("/public")
  public ResponseEntity<ChannelDto> create(
      @RequestBody @Valid PublicChannelCreateRequest requestBody) {
    PublicChannelCreateCommand command = ChannelDtoMapper.toCreatePublicChannelCommand(
        requestBody);
    ChannelDto result = channelService.create(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @PostMapping("/private")
  public ResponseEntity<ChannelDto> create(
      @RequestBody @Valid PrivateChannelCreateRequest requestBody) {
    PrivateChannelCreateCommand command = ChannelDtoMapper.toCreatePrivateChannelCommand(
        requestBody);
    ChannelDto result = channelService.create(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelDto> result = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(result);
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDto> update(@PathVariable UUID channelId,
      @RequestBody @Valid ChannelUpdateRequest requestBody) {
    ChannelUpdateCommand command = ChannelDtoMapper.toUpdateChannelCommand(channelId, requestBody);
    ChannelDto result = channelService.update(command);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<ChannelDeleteResponse> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.ok(new ChannelDeleteResponse(true));
  }
}
