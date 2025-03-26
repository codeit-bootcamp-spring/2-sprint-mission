package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ApiResponse<Channel>> createPublicChannel(@Valid @RequestBody PublicChannelCreateRequest request) {
        Channel response = channelService.createPublicChannel(request);
        ApiResponse<Channel> apiResponse = new ApiResponse<>("퍼블릭 채널 생성 성공", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/private")
    public ResponseEntity<ApiResponse<Channel>> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        Channel response = channelService.createPrivateChannel(request);
        ApiResponse<Channel> apiResponse = new ApiResponse<>("프라이빗 채널 생성 성공", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Channel>> update(@Valid @RequestBody ChannelUpdateRequest request) {
        Channel response = channelService.update(request);
        ApiResponse<Channel> apiResponse = new ApiResponse<>("채널 수정 성공", response);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        ApiResponse<Void> apiResponse = new ApiResponse<>("채널 삭제 성공", null);
        return ResponseEntity.ok(apiResponse);
    }

    //GET users/{userId}/channels
    //GET channels?userId=...
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChannelFindResponse>>> findAllByUser(@RequestParam UUID userId) {
        List<ChannelFindResponse> response = channelService.findAllByUserId(userId);
        ApiResponse<List<ChannelFindResponse>> apiResponse = new ApiResponse<>("유저 채널 목록 조회 성공", response);
        return ResponseEntity.ok(apiResponse);
    }
}
