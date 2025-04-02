package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.ChannelIdDto;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.request.ChannelPublicCreateDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelFindAllUserIdDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.user.request.UserIdDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    @GetMapping
    public ResponseEntity<List<ChannelFindAllUserIdDto>> channelFindAll(@RequestBody @Validated UserIdDto userIdDto){
        return ResponseEntity.ok(channelService.findAllByUserId(userIdDto.userId()));
    }

    @PostMapping
    public ResponseEntity<ChannelResponse> channelCreate(@RequestBody @Validated ChannelPublicCreateDto channelCreateDto){
        channelService.create(channelCreateDto);
        return ResponseEntity.ok(new ChannelResponse(true, "채널 생성 성공"));
    }

    @PatchMapping
    public ResponseEntity<ChannelResponse> channelUpdate(@RequestBody @Validated ChannelUpdateDto channelUpdateDto){
        channelService.update(channelUpdateDto);
        return ResponseEntity.ok(new ChannelResponse(true, "채널 수정 성공"));
    }

    @DeleteMapping
    public ResponseEntity<ChannelResponse> channelDelete(@RequestBody @Validated ChannelIdDto channelIdDto){
        channelService.delete(channelIdDto.channelId());
        return ResponseEntity.ok(new ChannelResponse(true, "채널 삭제 성공"));
    }

}
