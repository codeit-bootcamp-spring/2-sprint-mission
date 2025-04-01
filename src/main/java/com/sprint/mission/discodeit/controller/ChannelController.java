package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.channeldto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;


    @PostMapping("/createPublic")
    public ResponseEntity<Channel> createPublicChannel(
            @RequestBody ChannelCreateDto channelCreateRequest
    ) {
        Channel publicChannel  = channelService.createPublic(channelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(publicChannel);
    }

    @PostMapping("/createPrivate")
    public ResponseEntity<Channel> createPrivateChannel(
            @RequestBody ChannelCreateDto channelCreateRequest
    ) {
        Channel privateChannel = channelService.createPrivate(channelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(privateChannel);
    }


    @PutMapping("/update")
    public ResponseEntity<Channel> updateChannel(
            @RequestBody ChannelUpdateDto channelUpdateRequest
    ) {

        Channel updateChannel = channelService.update(channelUpdateRequest);
        return ResponseEntity.ok(updateChannel);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Channel> deleteChannel(
            @RequestBody ChannelDeleteDto ChannelDeleteRequest
    ) {
        channelService.delete(ChannelDeleteRequest);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/find")
    public ResponseEntity<ChannelFindResponseDto> findChannel(
            @RequestBody ChannelFindRequestDto channelFindRequest
    ) {
        ChannelFindResponseDto channelFindResponse = channelService.find(channelFindRequest);
        return ResponseEntity.ok(channelFindResponse);
    }


    @GetMapping("/findAllByUserId")
    public ResponseEntity<List<ChannelFindAllByUserIdResponseDto>> findChannelByUserId(
            @RequestBody ChannelFindAllByUserIdRequestDto channelFindAllByUserIdRequest
    ) {
        List<ChannelFindAllByUserIdResponseDto> channelFindAllByUserIdResponse = channelService.findAllByUserId(channelFindAllByUserIdRequest);
        return ResponseEntity.ok(channelFindAllByUserIdResponse);
    }
}
