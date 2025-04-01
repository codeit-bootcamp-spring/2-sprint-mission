package com.sprint.mission.discodeit.adapter.inbound.channel;


import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelCreateResult;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelDisplayList;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PublicChannelCreateRequestDTO;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.usecase.ChannelService;
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
@RequestMapping("/api/{userId}")
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/create")
  public ResponseEntity<ChannelCreateResult> create(@PathVariable UUID userId,
      @RequestBody PublicChannelCreateRequestDTO channelCreateDTO) {
    Channel channel = channelService.create(userId, channelCreateDTO);
    return ResponseEntity.ok(new ChannelCreateResult(channel.getChannelId()));
  }

  @GetMapping
  public ResponseEntity<ChannelDisplayList> findAll(@PathVariable UUID userId) {

    List<ChannelFindDTO> list = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(new ChannelDisplayList(list));
  }

  @PutMapping("/join/{channelId}")
  public ResponseEntity<String> join(@PathVariable UUID userId, @PathVariable UUID channelId) {
    channelService.join(channelId, userId);

    return ResponseEntity.ok("Success");
  }

  @PutMapping("/quit/{channelId}")
  public ResponseEntity<String> quit(@PathVariable UUID userId, @PathVariable UUID channelId) {
    channelService.quit(channelId, userId);

    return ResponseEntity.ok("Success");
  }

//  @PutMapping("/update/{channelId}")
//  public ResponseEntity<UUID> update(@PathVariable UUID channelId,
//      @RequestBody UpdateChannelDTO updateChannelDTO) {
//    UUID update = channelService.update(channelId, updateChannelDTO);
//    return ResponseEntity.ok(update);
//  }

  @DeleteMapping("/delete/{channelId}")
  public ResponseEntity<String> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.ok("Delete successful");
  }
}
