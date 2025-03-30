package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ApiResponse<SaveChannelResponseDto>> createPublic(
            @RequestBody SaveChannelRequestDto saveChannelRequestDto
    ) {
        SaveChannelResponseDto saveChannelResponseDto = channelService.createPublicChannel(saveChannelRequestDto);
        return ResponseEntity.ok(ApiResponse.success(saveChannelResponseDto));
    }

    @PostMapping("/private")
    public ResponseEntity<ApiResponse<SaveChannelResponseDto>> createPrivate(
            @RequestBody SaveChannelRequestDto saveChannelRequestDto
    ) {
        SaveChannelResponseDto saveChannelResponseDto = channelService.createPrivateChannel(saveChannelRequestDto);
        return ResponseEntity.ok(ApiResponse.success(saveChannelResponseDto));
    }

    @PutMapping("/{channelId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable UUID channelId,
            @RequestBody UpdateChannelRequestDto updateChannelRequestDto
    ) {
        channelService.updateChannel(channelId, updateChannelRequestDto);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID channelId
    ) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<FindChannelDto>>> findMyChannel(
            @PathVariable UUID userId
    ) {
        List<FindChannelDto> findMyChannelList = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(findMyChannelList));
    }
}
