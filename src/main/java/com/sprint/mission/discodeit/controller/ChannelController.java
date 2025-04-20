package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @PostMapping(path = "public")
    public ResponseEntity<ChannelDto> create(@RequestBody PublicChannelCreateRequest request) {
        UUID fixedOwnerId = UUID.fromString("e208797d-0eea-4575-bbc3-2af65c468125");
        PublicChannelCreateRequest fixedRequest = new PublicChannelCreateRequest(
            request.name(),
            request.description(),
            fixedOwnerId
        );
        ChannelDto createdChannel = channelService.create(fixedRequest);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdChannel);
    }

    @PostMapping(path = "private")
    public ResponseEntity<ChannelDto> create(@RequestBody PrivateChannelCreateRequest request) {
        UUID fixedOwnerId = UUID.fromString("e208797d-0eea-4575-bbc3-2af65c468125");
        PrivateChannelCreateRequest fixedRequest = new PrivateChannelCreateRequest(
            fixedOwnerId,
            request.participantIds(),
            request.channelName()
        );
        ChannelDto createdChannel = channelService.create(fixedRequest);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdChannel);
    }

    @PatchMapping(path = "{channelId}")
    public ResponseEntity<ChannelDto> update(@PathVariable("channelId") UUID channelId,
        @RequestBody PublicChannelUpdateRequest request) {
        ChannelDto udpatedChannelDto = channelService.update(channelId, request);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(udpatedChannelDto);
    }

    @DeleteMapping(path = "{channelId}")
    public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channels);
    }
}
