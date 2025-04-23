package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/api/channels/public")
    public ResponseEntity<ChannelDto> createPublicChannel(
        @RequestBody CreatePublicChannelRequest request
    ) {
        ChannelDto response = channelService.createPublicChannel(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/channels/private")
    public ResponseEntity<ChannelDto> createPrivateChannel(
        @RequestBody CreatePrivateChannelRequest request
    ) {
        ChannelDto response = channelService.createPrivateChannel(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/channels")
    public ResponseEntity<List<ChannelDto>> getChannelByUserId(
        @RequestParam UUID userId
    ) {
        List<ChannelDto> response = channelService.findAllByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/channels/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(
        @PathVariable UUID channelId,
        @RequestBody UpdateChannelRequest request) {
        ChannelDto response = channelService.update(channelId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/channels/{channelId}")
    public ResponseEntity<Void> deleteChannel(
        @PathVariable UUID channelId
    ) {
        channelService.remove(channelId);

        return ResponseEntity.noContent().build();
    }
}
