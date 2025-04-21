package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @Operation(summary = "공개 채널 생성")
    @PostMapping("/public")
    @ApiResponse(responseCode = "201", description = "공개 채널 생성 성공")
    public ResponseEntity<ChannelDto> createPublicChannel(
        @RequestBody PublicChannelCreateRequest request) {
        UUID channelId = channelService.createPublicChannel(request).getId();
        return channelService.getChannelById(channelId)
            .map(response -> ResponseEntity.status(201).body(response))
            .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "비공개 채널 생성")
    @PostMapping("/private")
    @ApiResponse(responseCode = "201", description = "비공개 채널 생성 성공")
    public ResponseEntity<ChannelDto> createPrivateChannel(
        @RequestBody PrivateChannelCreateRequest request) {
        UUID channelId = channelService.createPrivateChannel(request).getId();
        return channelService.getChannelById(channelId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.badRequest().build());
    }


    @Operation(summary = "채널 수정")
    @ApiResponse(
        responseCode = "200",
        description = "채널 수정 성공",
        content = @Content(mediaType = "*/*")
    )
    @PatchMapping(path = "/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(
        @PathVariable UUID channelId,
        @RequestBody UpdateChannelRequest request) {

        if (!channelId.equals(request.channelId())) {
            return ResponseEntity.badRequest().build();
        }
        channelService.updateChannel(request);

        return channelService.getChannelById(channelId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.noContent().build());
    }

    @Operation(summary = "채널 삭제")
    @DeleteMapping("/{channelId}")
    @ApiResponse(responseCode = "204", description = "채널 삭제 성공")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "참여 중인 채널 목록 조회", description = "사용자의 ID를 기반으로 자신이 참여 중인 채널 조회")
    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAllByUser(@RequestParam UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}
