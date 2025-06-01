package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping(path = "public")
    public ResponseEntity<ChannelDto> create(
        @Valid @RequestBody PublicChannelCreateRequest request) {

        ChannelDto createdChannel = channelService.create(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdChannel);
    }

    @PostMapping(path = "private")
    public ResponseEntity<ChannelDto> create(
        @Valid @RequestBody PrivateChannelCreateRequest request) {
        ChannelDto createdChannel = channelService.create(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdChannel);
    }

    @PatchMapping(path = "{channelId}")
    public ResponseEntity<ChannelDto> update(@PathVariable("channelId") UUID channelId,
        @Valid @RequestBody PublicChannelUpdateRequest request) {
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
