package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelUpdateRequest;
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
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final UserService userService;

    @PostMapping("/public")
    public ResponseEntity<ChannelResult> createPublic(
            @Valid @RequestBody PublicChannelCreateRequest channelRegisterRequest) {
        return ResponseEntity.ok(channelService.createPublic(channelRegisterRequest));
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResult> createPrivate(
            @Valid @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
        return ResponseEntity.ok(channelService.createPrivate(privateChannelCreateRequest));
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelResult> getById(@PathVariable UUID channelId) {
        return ResponseEntity.ok(channelService.getById(channelId));
    }

    @GetMapping
    public ResponseEntity<List<ChannelResult>> getAllByUserId(
            @RequestParam(value = "userId", required = false) UUID userId) {
        return ResponseEntity.ok(channelService.getAllByUserId(userId));
    }

    @PutMapping("/public/update")
    public ResponseEntity<ChannelResult> updatePublicChannelName(
            @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
        ChannelResult channelResult = channelService.updatePublicChannelName(
                publicChannelUpdateRequest.channelId(), publicChannelUpdateRequest.channelName());

        return ResponseEntity.ok(channelResult);
    }

    @PostMapping("/private/{channelId}/members")
    public ResponseEntity<ChannelResult> addPrivateChannelMember(@PathVariable UUID channelId, @RequestBody String friendEmail) {
        UserResult friend = userService.getByEmail(friendEmail);
        ChannelResult channelResult = channelService.addPrivateChannelMember(channelId, friend.id());

        return ResponseEntity.ok(channelResult);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);

        return ResponseEntity.noContent().build();
    }
}
