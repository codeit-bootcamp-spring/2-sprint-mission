package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.display.ChannelDisplayList;
import com.sprint.mission.discodeit.dto.create.PublicChannelCreateRequestDTO;
import com.sprint.mission.discodeit.dto.update.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{serverId}/channels")
public class ChannelController {
    private final ChannelService channelService;
    private final ReadStatusService readStatusService;

    @PostMapping("/create")
    public ResponseEntity<UUID> create(@RequestBody PublicChannelCreateRequestDTO channelCreateDTO, HttpServletRequest httpRequest ) {
        UUID userId = (UUID)httpRequest.getAttribute("userId");
        Channel channel = channelService.create(userId, channelCreateDTO);
        return ResponseEntity.ok(channel.getChannelId());
    }

    @GetMapping
    public ResponseEntity<ChannelDisplayList> findAll(HttpServletRequest httpRequest) {
        UUID userId = (UUID)httpRequest.getAttribute("userId");
        List<ChannelFindDTO> list = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(new ChannelDisplayList(list));
    }
    @PutMapping("/{channelId}/join")
    public ResponseEntity<String> join(@PathVariable UUID channelId, HttpServletRequest httpRequest  ) {
        UUID userId = (UUID)httpRequest.getAttribute("userId");

        channelService.join(channelId,userId);

        return ResponseEntity.ok("Success");
    }

    @PutMapping("/{channelId}/quit")
    public ResponseEntity<String> quit(@PathVariable UUID channelId, HttpServletRequest httpRequest   ) {
        UUID userId = (UUID)httpRequest.getAttribute("userId");

        channelService.quit(channelId,userId);

        return ResponseEntity.ok("Success");
    }

    @PutMapping("/{channelId}/update")
    public ResponseEntity<UUID> update(@PathVariable UUID channelId, @RequestBody UpdateChannelDTO updateChannelDTO, HttpServletRequest httpRequest ) {
        UUID userId = (UUID)httpRequest.getAttribute("userId");
        UUID update = channelService.update(channelId, updateChannelDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{channelId}/delete")
    public ResponseEntity<String> delete(@PathVariable UUID channelId, HttpServletRequest httpRequest ) {
        UUID userId = (UUID)httpRequest.getAttribute("userId");
        channelService.delete(channelId);
        return ResponseEntity.ok("Delete successful");

    }

    @PutMapping("/{channelId}/read-status")
    public ResponseEntity<Void> updateReadStatus(
            @PathVariable UUID channelId,
            @RequestBody ReadStatusUpdateRequestDTO dto,
            HttpServletRequest request
    ) {
        UUID userId = (UUID) request.getAttribute("userId");

        readStatusService.update(channelId, userId, dto);
        return ResponseEntity.ok().build();
    }
}
