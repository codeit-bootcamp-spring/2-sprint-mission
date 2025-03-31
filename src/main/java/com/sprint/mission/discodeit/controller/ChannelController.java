package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.channel.*;
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
    public ResponseEntity<ChannelResult> createPublicChannel(@Valid @RequestBody ChannelCreateRequest channelRegisterRequest) {
        return ResponseEntity.ok(channelService.createPublic(channelRegisterRequest));
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResult> createPrivateChannel(@Valid @RequestBody PrivateChannelCreationRequest privateChannelCreationRequest) {
        return ResponseEntity.ok(channelService.createPrivate(privateChannelCreationRequest));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChannelResult>> getAllByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(channelService.getAllByUserId(userId));
    }

    @PutMapping("/public/update")
    public ResponseEntity<ChannelResult> updatePublicChannelName(@RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
        return ResponseEntity.ok(channelService.updatePublicChannelName(publicChannelUpdateRequest.channelId(), publicChannelUpdateRequest.channelName()));
    }

    @PostMapping("/private/members")
    public ResponseEntity<ChannelResult> addPrivateChannelMember(@RequestBody PrivateChannelAddMemeberRequest privateChannelAddMemeberRequest) {
        UserResult friend = userService.getByEmail(privateChannelAddMemeberRequest.friendEmail());

        return ResponseEntity.ok(channelService.addPrivateChannelMember(privateChannelAddMemeberRequest.channelId(), friend.id()));
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelResult> getById(@PathVariable UUID channelId) {
        return ResponseEntity.ok(channelService.getById(channelId));
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }
}
