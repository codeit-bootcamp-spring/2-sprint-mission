package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    // 공개 채널 생성
    @PostMapping("/public")
    public ResponseEntity<Channel> createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.ok(channel);
    }

    // 비공개 채널 생성
    @PostMapping("/private")
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.ok(channel);
    }

    // 특정 채널 조회
    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelDto> getChannel(@PathVariable UUID channelId) {
        ChannelDto channelDto = channelService.find(channelId);
        return ResponseEntity.ok(channelDto);
    }

    // 사용자가 참여한 모든 채널 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChannelDto>> getUserChannels(@PathVariable UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }

    // 공개 채널 수정
    @PutMapping("/{channelId}")
    public ResponseEntity<Channel> updateChannel(@PathVariable UUID channelId, @RequestBody PublicChannelUpdateRequest request) {
        Channel updatedChannel = channelService.update(channelId, request);
        return ResponseEntity.ok(updatedChannel);
    }

    // 채널 삭제
    @DeleteMapping("/{channelId}")
    public ResponseEntity<String> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.ok("채널이 삭제되었습니다.");
    }
}
