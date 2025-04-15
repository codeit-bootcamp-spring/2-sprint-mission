package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @PostMapping("/private")
    public ResponseEntity<Channel> createPrivate(@RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<Channel> updateChannel(
            @PathVariable UUID channelId,
            @RequestBody PublicChannelUpdateRequest request) {
        Channel updatedChannel = channelService.updateChannel(channelId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedChannel);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannelById(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAllChannelsByUserId(@RequestParam UUID userId) {
        List<ChannelDto> channels = channelService.findAllChannelsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
}
