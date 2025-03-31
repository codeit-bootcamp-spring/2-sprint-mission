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


    @PostMapping("/create")
    public ResponseEntity<Channel> createChannel(
            @RequestPart("channelInfo") ChannelCreateDto channelCreateReq
    ) {
        if (channelCreateReq.channelType() == ChannelType.PRIVATE) {
            Channel privateChannel = channelService.createPrivate(channelCreateReq);
            return ResponseEntity.status(HttpStatus.CREATED).body(privateChannel);
        } else {
            Channel publicChannel = channelService.createPublic(channelCreateReq);
            return ResponseEntity.status(HttpStatus.CREATED).body(publicChannel);
        }
    }


    @PutMapping("/update")
    public ResponseEntity<Channel> updateChannel(
            @RequestPart("newChannelInfo") ChannelUpdateDto channelUpdateReq
    ) {

        Channel updateChannel = channelService.update(channelUpdateReq);
        return ResponseEntity.ok(updateChannel);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Channel> deleteChannel(
            @RequestPart("channelInfo") ChannelDeleteDto ChannelDeleteReq
    ) {
        channelService.delete(ChannelDeleteReq);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/find")
    public ResponseEntity<ChannelFindResponseDto> findChannel(
            @RequestPart("channelInfo") ChannelFindRequestDto channelFindReq
    ) {
        ChannelFindResponseDto channelFindResponse = channelService.find(channelFindReq);
        return ResponseEntity.ok(channelFindResponse);
    }


    @GetMapping("/findAllByUserId")
    public ResponseEntity<List<ChannelFindAllByUserIdResponseDto>> findChannelByUserId(
            @RequestPart("userId") ChannelFindAllByUserIdRequestDto channelFindAllByUserIdReq
    ) {
        List<ChannelFindAllByUserIdResponseDto> channelFindAllByUserIdResponse = channelService.findAllByUserId(channelFindAllByUserIdReq);
        return ResponseEntity.ok(channelFindAllByUserIdResponse);
    }
}
