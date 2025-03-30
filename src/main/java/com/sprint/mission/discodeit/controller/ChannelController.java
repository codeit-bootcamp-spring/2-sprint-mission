package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequiresAuth
    @PostMapping("/public")
    public ResponseEntity<ChannelDto.Response> createPublicChannel(@Valid @RequestBody ChannelDto.CreatePublic channelDto) {
        return ResponseEntity.ok(channelService.createPublicChannel(channelDto));
    }
    
    @RequiresAuth
    @PostMapping("/private")
    public ResponseEntity<ChannelDto.Response> createPrivateChannel(@Valid @RequestBody ChannelDto.CreatePrivate channelDto) {
        return ResponseEntity.ok(channelService.createPrivateChannel(channelDto));
    }
    
    @RequiresAuth
    @PutMapping("/update/{channelId}")
    public ResponseEntity<ChannelDto.Response> updateChannel(
            @PathVariable UUID channelId,
            @Valid @RequestBody ChannelDto.Update channelDto) {

        ChannelDto.Response updatedChannel = channelService.updateChannel(channelDto, channelId);
        return ResponseEntity.ok(updatedChannel);
    }
    
    @RequiresAuth
    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<ChannelDto.DeleteResponse> deleteChannel(
          @Valid  @PathVariable UUID channelId,HttpServletRequest httpRequest) {
        String ownerId = (String) httpRequest.getAttribute("userId");
        boolean success = channelService.deleteChannel(channelId, UUID.fromString(ownerId));

        return ResponseEntity.ok(new ChannelDto.DeleteResponse(channelId, String.valueOf(success)));

    }
    @GetMapping("/find/{userId}")
    public ResponseEntity<List<ChannelDto.Response>> getChannelsForUser(@Valid @PathVariable UUID userId) {

        List<ChannelDto.Response> channels = channelService.getAccessibleChannels(userId);
        return ResponseEntity.ok(channels);
    }
    
    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelDto.Response> getChannel(@Valid @PathVariable UUID channelId) {
        
        ChannelDto.Response channel = channelService.findById(channelId);
        return ResponseEntity.ok(channel);
    }
}