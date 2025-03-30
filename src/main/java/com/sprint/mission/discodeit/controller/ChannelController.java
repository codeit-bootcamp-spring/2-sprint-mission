package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequestDto request) {
        Channel channel = channelService.createPublic(request);
        return ResponseEntity.ok(channel);
    }

    @PostMapping("/private")
    public ResponseEntity<Channel> createPrivate(@RequestBody PrivateChannelCreateRequestDto request) {
        Channel channel = channelService.createPrivate(request);
        return ResponseEntity.ok(channel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelResponseDto> find(@PathVariable("id") UUID channelId) {
        ChannelResponseDto channel = channelService.find(channelId);
        return ResponseEntity.ok(channel);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChannelResponseDto>> findAllByUser(@PathVariable("userId") UUID userId) {
        List<ChannelResponseDto> channels = channelService.findAllByUserID(userId);
        return ResponseEntity.ok(channels);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Channel> update(@PathVariable("id") UUID channelId, @RequestBody ChannelUpdateRequestDto request) {
        Channel updatedChannel = channelService.update(request);
        return ResponseEntity.ok(updatedChannel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }
}
