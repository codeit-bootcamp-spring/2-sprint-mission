package com.sprint.mission.discodeit.controller;

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

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping(value = "/public", consumes = "application/json")
    public ResponseEntity<ChannelDto> createPublicChannel(@RequestBody PublicChannelCreateRequest publicChannelCreateRequest){
        Channel createdChannel = channelService.create(publicChannelCreateRequest);
        ChannelDto channeldto = channelService.find(createdChannel.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(channeldto);
    }

    @PostMapping(value = "/private", consumes = "application/json")
    public ResponseEntity<ChannelDto> createPrivateChannel(@RequestBody PrivateChannelCreateRequest privateChannelCreateRequest){
        Channel createdChannel = channelService.create(privateChannelCreateRequest);
        ChannelDto channeldto = channelService.find(createdChannel.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(channeldto);
    }

    @PatchMapping(value ="/{channelId}", consumes = "application/json")
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable("channelId") UUID channelId, @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest){
        Channel updatedChannel = channelService.update(channelId,publicChannelUpdateRequest);
        ChannelDto channeldto = channelService.find(updatedChannel.getId());

        return ResponseEntity.ok(channeldto);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<ChannelDto> deleteChannel(@PathVariable("channelId") UUID channelId){
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity<List<ChannelDto>> getAllChannelUser(@RequestParam UUID userId){
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }


}

