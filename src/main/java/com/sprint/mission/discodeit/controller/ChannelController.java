package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.create.PublicChannelCreateRequestDTO;
import com.sprint.mission.discodeit.dto.display.ChannelDisplayList;
import com.sprint.mission.discodeit.dto.update.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{userId}/channels")
public class ChannelController {
    private final ChannelService channelService;
    private final ReadStatusService readStatusService;

    @PostMapping("/create")
    public ResponseEntity<UUID> create(@PathVariable UUID userId, @RequestBody PublicChannelCreateRequestDTO channelCreateDTO) {
        Channel channel = channelService.create(userId, channelCreateDTO);
        return ResponseEntity.ok(channel.getChannelId());
    }

    @GetMapping
    public ResponseEntity<ChannelDisplayList> findAll(@PathVariable UUID userId) {

        List<ChannelFindDTO> list = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(new ChannelDisplayList(list));
    }
    @PutMapping("/{channelId}/join")
    public ResponseEntity<String> join(@PathVariable UUID userId, @PathVariable UUID channelId) {
        channelService.join(channelId,userId);

        return ResponseEntity.ok("Success");
    }

    @PutMapping("/{channelId}/quit")
    public ResponseEntity<String> quit(@PathVariable UUID userId, @PathVariable UUID channelId) {
        channelService.quit(channelId,userId);

        return ResponseEntity.ok("Success");
    }

    @PutMapping("/{channelId}/update")
    public ResponseEntity<UUID> update(@PathVariable UUID userId, @PathVariable UUID channelId, @RequestBody UpdateChannelDTO updateChannelDTO) {
        UUID update = channelService.update(channelId, updateChannelDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{channelId}/delete")
    public ResponseEntity<String> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.ok("Delete successful");

    }

    @PutMapping("/{channelId}/read-status")
    public ResponseEntity<Void> updateReadStatus(
            @PathVariable UUID userId,
            @PathVariable UUID channelId,
            @RequestBody ReadStatusUpdateRequestDTO dto
    ) {
        readStatusService.update(channelId, userId, dto);
        return ResponseEntity.ok().build();
    }
}
