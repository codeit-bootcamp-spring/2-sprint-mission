package com.sprint.mission.discodeit.adapter.inbound.channel;

import com.sprint.mission.discodeit.adapter.inbound.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.response.ChannelDeleteResponse;
import com.sprint.mission.discodeit.adapter.inbound.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.core.channel.usecase.ChannelService;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelListResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
public class ChannelController {

  private final ChannelDtoMapper channelDtoMapper;
  private final ChannelService channelService;

  @PostMapping("/public")
  public ResponseEntity<ChannelResponse> create(
      @RequestBody @Valid PublicChannelCreateRequest requestBody) {
    CreatePublicChannelCommand command = channelDtoMapper.toCreatePublicChannelCommand(
        requestBody);
    ChannelResult result = channelService.create(command);
    ChannelResponse response = channelDtoMapper.toCreateResponse(result);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/private")
  public ResponseEntity<ChannelResponse> create(
      @RequestBody PrivateChannelCreateRequest requestBody) {
    CreatePrivateChannelCommand command = channelDtoMapper.toCreatePrivateChannelCommand(
        requestBody);
    ChannelResult result = channelService.create(command);

    return ResponseEntity.ok(channelDtoMapper.toCreateResponse(result));
  }

  @GetMapping
  public ResponseEntity<List<ChannelResult>> findAll(@RequestParam("userId") UUID userId) {
    ChannelListResult result = channelService.findChannelsByUserId(userId);

    return ResponseEntity.ok(result.channelList());
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelResponse> update(@PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest requestBody) {
    UpdateChannelCommand command = channelDtoMapper.toUpdateChannelCommand(channelId, requestBody);
    ChannelResult result = channelService.update(command);
    return ResponseEntity.ok(channelDtoMapper.toCreateResponse(result));
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<ChannelDeleteResponse> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.ok(new ChannelDeleteResponse(true));
  }

}
