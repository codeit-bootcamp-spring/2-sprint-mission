package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
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

    @Override
    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublic(@RequestBody PublicChannelCreateRequest request) {
        ChannelDto channel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @Override
    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createPrivate(@RequestBody PrivateChannelCreateRequest request) {
        ChannelDto channel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @Override
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(
            @PathVariable("channelId") UUID channelId,
            @RequestBody PublicChannelUpdateRequest request) {
        ChannelDto updatedChannel = channelService.updateChannel(channelId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedChannel);
    }

    @Override
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannelById(@PathVariable("channelId") UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAllChannelsByUserId(@RequestParam("userId") UUID userId) {
        List<ChannelDto> channels = channelService.findAllChannelsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
}
