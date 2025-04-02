package com.sprint.mission.discodeit.adapter.inbound.channel;


import static com.sprint.mission.discodeit.adapter.inbound.channel.ChannelDtoMapper.toCreatePrivateChannelCommand;
import static com.sprint.mission.discodeit.adapter.inbound.channel.ChannelDtoMapper.toCreatePublicChannelCommand;
import static com.sprint.mission.discodeit.adapter.inbound.channel.ChannelDtoMapper.toUpdateChannelCommand;

import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelCreateResponse;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelDeleteResponse;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelUpdateResponse;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.usecase.ChannelService;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelListResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/channels")
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/createPublic")
  public ResponseEntity<ChannelCreateResponse> create(@PathVariable UUID userId,
      @RequestBody PublicChannelCreateRequest requestBody) {
    CreatePublicChannelCommand command = toCreatePublicChannelCommand(userId,
        requestBody);
    channelService.create(command);

    return ResponseEntity.ok(new ChannelCreateResponse(true, "Public Channel Create Successfully"));
  }

  @PostMapping("/createPrivate")
  public ResponseEntity<ChannelCreateResponse> create(@PathVariable UUID userId,
      @RequestBody PrivateChannelCreateRequest requestBody) {
    CreatePrivateChannelCommand command = toCreatePrivateChannelCommand(userId,
        requestBody);
    channelService.create(command);

    return ResponseEntity.ok(
        new ChannelCreateResponse(true, "Private Channel Create Successfully"));
  }

  @GetMapping
  public ResponseEntity<List<ChannelResult>> findAll(@PathVariable UUID userId) {
    ChannelListResult result = channelService.findChannelsByUserId(userId);
    //TODO. 25.04.02 Channel findAll 더 좋은 API 설계가 떠오르지 않아 임시로 List<ChannelResult> 반환
    return ResponseEntity.ok(result.channelList());
  }

  @PutMapping("/{channelId}")
  public ResponseEntity<ChannelUpdateResponse> update(@PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest requestBody) {
    UpdateChannelCommand command = toUpdateChannelCommand(channelId, requestBody);
    channelService.update(command);
    return ResponseEntity.ok(new ChannelUpdateResponse(true));
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<ChannelDeleteResponse> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.ok(new ChannelDeleteResponse(true));
  }

  //  @PutMapping("/join/{channelId}")
//  public ResponseEntity<String> join(@PathVariable UUID userId, @PathVariable UUID channelId) {
//    channelService.join(channelId, userId);
//
//    return ResponseEntity.ok("Success");
//  }
//
//  @PutMapping("/quit/{channelId}")
//  public ResponseEntity<String> quit(@PathVariable UUID userId, @PathVariable UUID channelId) {
//    channelService.quit(channelId, userId);
//
//    return ResponseEntity.ok("Success");
//  }
}
