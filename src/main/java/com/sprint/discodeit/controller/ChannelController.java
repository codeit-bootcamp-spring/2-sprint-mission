package com.sprint.discodeit.controller;

import com.sprint.discodeit.domain.ChannelType;
import com.sprint.discodeit.domain.dto.channelDto.ChannelCreateRequestDto;
import com.sprint.discodeit.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.domain.dto.channelDto.ChannelUpdateRequestDto;
import com.sprint.discodeit.domain.entity.Channel;
import com.sprint.discodeit.service.ChannelServiceV1;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelServiceV1 channelService;


    @PostMapping
    public ResponseEntity<ChannelResponseDto> createChannel(@RequestBody ChannelCreateRequestDto requestDto) {
        ChannelResponseDto responseDto = channelService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @PutMapping("/{channelId}")
    public ResponseEntity<Channel> updateChannel(@PathVariable String channelId,
                                                            @RequestBody ChannelUpdateRequestDto requestDto) {
        Channel responseDto = channelService.update(channelId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 채널 삭제
    @DeleteMapping("/{channelId}")
    public ResponseEntity<String> deleteChannel(@PathVariable String channelId) {
        channelService.delete(UUID.fromString(channelId));
        return ResponseEntity.ok("채널방 삭제되었습니다");
    }

    @GetMapping("/user/getChannelType")
    public ResponseEntity<List<Channel>> getChannelsForUser(@RequestParam ChannelType channelType) {
        List<Channel> channels = channelService.find(channelType);
        return ResponseEntity.ok(channels);
    }
}
