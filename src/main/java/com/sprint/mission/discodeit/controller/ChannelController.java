package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.AddChannelMemberRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
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
    public ResponseEntity<ChannelResponseDto> createPublicChannel(
        @RequestBody CreatePublicChannelRequest request
    ) {

        UUID channelId = channelService.createPublicChannel(request);
        ChannelResponseDto response = channelService.findByChannelId(channelId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/channels/private")
    public ResponseEntity<ChannelResponseDto> createPrivateChannel(
        @RequestBody CreatePrivateChannelRequest request
    ) {

        UUID channelId = channelService.createPrivateChannel(request);
        ChannelResponseDto response = channelService.findByChannelId(channelId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/channels")
    public ResponseEntity<List<ChannelResponseDto>> getChannelByUserId(
        @RequestParam UUID userId
    ) {
        List<ChannelResponseDto> response = channelService.findAllByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/channels/{channelId}")
    public ResponseEntity<ChannelResponseDto> updateChannel(
        @PathVariable UUID channelId,
        @RequestBody UpdateChannelRequest request) {

        channelService.update(channelId, request);
        ChannelResponseDto response = channelService.findByChannelId(channelId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/channels/{channelId}")
    public ResponseEntity<Void> deleteChannel(
        @PathVariable UUID channelId
    ) {
        channelService.remove(channelId);

        return ResponseEntity.noContent().build();
    }

    // 채널 멤버 관리
    @PostMapping("/api/channels/{channelId}/members")
    public ResponseEntity<Void> addChannelMember(
        @PathVariable UUID channeld,
        @RequestBody AddChannelMemberRequest request) {
        UUID channelId = request.getChannelId();
        UUID userId = request.getUserId();

        channelService.addMember(channelId, userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/channels/{channelId}/members/{userId}")
    public ResponseEntity<Void> removeChannelMember(
        @PathVariable UUID channelId,
        @PathVariable UUID userId) {

        channelService.removeMember(channelId, userId);

        return ResponseEntity.noContent().build();
    }
}
