package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("/public")
    public Channel createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        return channelService.create(request);
    }

    @PostMapping("/private")
    public Channel createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        return channelService.create(request);
    }

    @PutMapping("/{channelId}")
    public Channel updatePublicChannel(@PathVariable UUID channelId, @RequestBody PublicChannelUpdateRequest request) {
        return channelService.update(channelId, request);
    }

    @DeleteMapping("/{channelId}")
    public void deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
    }

    @GetMapping("/user/{userId}")
    public List<ChannelDto> getUserChannels(@PathVariable UUID userId) {
        return channelService.findAllByUserId(userId);
    }
}
