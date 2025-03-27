package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/createPrivate")
    public ResponseEntity<ApiResponse<ChannelCreateResponse>> cratePrivateChannel(@Valid @RequestBody PrivateChannelCreateRequest request) {
        UUID channelId = channelService.createPrivateChannel(request);
        ApiResponse<ChannelCreateResponse> response = new ApiResponse<>(true, "Private 채널 생성 성공", new ChannelCreateResponse(channelId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/createPublic")
    public ResponseEntity<ApiResponse<ChannelCreateResponse>> createPublicChannel(@Valid @RequestBody PublicChannelCreateRequest request) {
        UUID channelId = channelService.createPublicChannel(request.channelName());
        ApiResponse<ChannelCreateResponse> response = new ApiResponse<>(true, "Public 채널 생성 성공", new ChannelCreateResponse(channelId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("{id}/updatePublic")
    public ResponseEntity<ApiResponse<ChannelUpdateResponse>> updatePublicChannel(@PathVariable UUID id, @Valid @RequestBody ChannelUpdateRequest request) {
        channelService.updateChannel(id, request);
        ApiResponse<ChannelUpdateResponse> response = new ApiResponse<>(true, "Public 채널 업데이트 성공", new ChannelUpdateResponse(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ChannelDeleteResponse>> deleteChannel(@PathVariable UUID id) {
        channelService.deleteChannel(id);
        ApiResponse<ChannelDeleteResponse> response = new ApiResponse<>(true, "채널 삭제 성공", new ChannelDeleteResponse(id, Instant.now()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }


}
