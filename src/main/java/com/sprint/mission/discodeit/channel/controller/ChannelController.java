package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.dto.service.ChannelResult;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ChannelResult> createPublic(@Valid @RequestBody PublicChannelCreateRequest channelRegisterRequest) {
        return ResponseEntity.ok(channelService.createPublic(channelRegisterRequest));
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResult> createPrivate(@Valid @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
        return ResponseEntity.ok(channelService.createPrivate(privateChannelCreateRequest));
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelResult> getById(@PathVariable UUID channelId) {

        return ResponseEntity.ok(channelService.getById(channelId));
    }

    @GetMapping
    public ResponseEntity<List<ChannelResult>> getAllByUserId(@RequestParam(value = "userId") UUID userId) {
        return ResponseEntity.ok(channelService.getAllByUserId(userId));
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelResult> updatePublic(@PathVariable UUID channelId, @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {

        ChannelResult channelResult = channelService.updatePublic(channelId, publicChannelUpdateRequest);

        return ResponseEntity.ok(channelResult);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);

        return ResponseEntity.noContent().build();
    }

}

