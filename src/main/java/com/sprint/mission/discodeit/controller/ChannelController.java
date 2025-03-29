package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ResponseEntity<ChannelDto> createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        return ResponseEntity.ok(channelService.find(channelService.create(request).getId()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ResponseEntity<ChannelDto> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        return ResponseEntity.ok(channelService.find(channelService.create(request).getId()));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/public/{channelId}")
    public ResponseEntity<ChannelDto> updatePublicChannel(@PathVariable UUID channelId, @RequestBody PublicChannelUpdateRequest request) {
        return ResponseEntity.ok(channelService.find(channelService.update(channelId, request).getId()));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    public ResponseEntity<List<ChannelDto>> getUserChannels(@PathVariable UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}