package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final UserService userService;

    @PostMapping("/public")
    public ChannelResult createPublicChannel(@Valid @RequestBody ChannelCreateRequest channelRegisterRequest) {
        return channelService.createPublic(channelRegisterRequest);
    }

    @PostMapping("/private")
    public ChannelResult createPrivateChannel(@Valid @RequestBody ChannelCreateRequest channelRegisterRequest, @RequestParam List<UUID> memberIds) {
        return channelService.createPrivate(channelRegisterRequest, memberIds);
    }

    @GetMapping("/user/{userId}")
    public List<ChannelResult> getAllByUserId(@PathVariable UUID userId) {
        return channelService.getAllByUserId(userId);
    }

    @PatchMapping("/{channelId}/public")
    public ChannelResult updatePublicChannelName(@PathVariable UUID channelId, @RequestParam String channelName) {
        return channelService.updatePublicChannelName(channelId, channelName);
    }

    @PostMapping("/{channelId}/members")
    public ChannelResult addPrivateChannelMember(@PathVariable UUID channelId, @RequestParam String friendEmail) {
        UserResult friend = userService.getByEmail(friendEmail);

        return channelService.addPrivateChannelMember(channelId, friend.id());
    }

    @GetMapping("/{channelId}")
    public ChannelResult getById(@PathVariable UUID channelId) {
        return channelService.getById(channelId);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }
}
