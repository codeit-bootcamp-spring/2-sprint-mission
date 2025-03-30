package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
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
    public ChannelRequest createPublicChannel(@RequestBody ChannelRegisterRequest channelRegisterRequest) {
        return channelService.createPublic(channelRegisterRequest);
    }

    @PostMapping("/private")
    public ChannelRequest createPrivateChannel(@RequestBody ChannelRegisterRequest channelRegisterRequest, @RequestParam List<UUID> memberIds) {
        return channelService.createPrivate(channelRegisterRequest, memberIds);
    }

    @GetMapping("/user/{userId}")
    public List<ChannelRequest> findAllByUserId(@PathVariable UUID userId) {
        return channelService.findAllByUserId(userId);
    }

    @PatchMapping("/{channelId}/public")
    public ChannelRequest updatePublicChannelName(@PathVariable UUID channelId, @RequestParam String channelName) {
        return channelService.updatePublicChannelName(channelId, channelName);
    }

    @PostMapping("/{channelId}/members")
    public ChannelRequest addPrivateChannelMember(@PathVariable UUID channelId, @RequestParam String friendEmail) {
        UserResult friend = userService.findByEmail(friendEmail);

        return channelService.addPrivateChannelMember(channelId, friend.id());
    }

    @GetMapping("/{channelId}")
    public ChannelRequest findById(@PathVariable UUID channelId) {
        return channelService.findById(channelId);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }
}
