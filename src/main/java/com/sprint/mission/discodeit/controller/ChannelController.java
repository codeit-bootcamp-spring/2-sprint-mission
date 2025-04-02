package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.create.PublicChannelCreateRequestDTO;
import com.sprint.mission.discodeit.dto.display.ChannelDisplayList;
import com.sprint.mission.discodeit.dto.result.ChannelCreateResult;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{userId}/{serverId}")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/create")
    public ResponseEntity<ChannelCreateResult> create(@PathVariable UUID userId, @PathVariable UUID serverId, @RequestBody PublicChannelCreateRequestDTO channelCreateDTO) {
        Channel channel = channelService.create(userId, serverId, channelCreateDTO);
        return ResponseEntity.ok(new ChannelCreateResult(channel.getChannelId()));
    }

    @GetMapping
    public ResponseEntity<ChannelDisplayList> findAll(@PathVariable UUID userId) {

        List<ChannelFindDTO> list = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(new ChannelDisplayList(list));
    }

    @PutMapping("/join/{channelId}")
    public ResponseEntity<String> join(@PathVariable UUID userId, @PathVariable UUID channelId) {
        channelService.join(channelId,userId);

        return ResponseEntity.ok("Success");
    }

    @PutMapping("/quit/{channelId}")
    public ResponseEntity<String> quit(@PathVariable UUID userId, @PathVariable UUID channelId) {
        channelService.quit(channelId,userId);

        return ResponseEntity.ok("Success");
    }

    @PutMapping("/update/{channelId}")
    public ResponseEntity<UUID> update( @PathVariable UUID channelId, @RequestBody UpdateChannelDTO updateChannelDTO) {
        UUID update = channelService.update(channelId, updateChannelDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<String> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.ok("Delete successful");
    }
}
