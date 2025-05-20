package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @Operation(summary = "공개 채널 생성")
    @PostMapping("/public")
    @ApiResponse(responseCode = "201", description = "공개 채널 생성 성공")
    public ResponseEntity<ChannelDto> createPublicChannel(
        @RequestBody @Valid PublicChannelCreateRequest request) {

        log.info("공개 채널 생성 요청 - name: {}", request.name());

        ChannelDto createdChannel = channelService.createPublicChannel(request);
        log.info("공개 채널 생성 완료 - channelId: {}", createdChannel.id());

        return ResponseEntity.status(201).body(createdChannel);
    }

    @Operation(summary = "비공개 채널 생성")
    @PostMapping("/private")
    @ApiResponse(responseCode = "201", description = "비공개 채널 생성 성공")
    public ResponseEntity<ChannelDto> createPrivateChannel(
        @RequestBody @Valid PrivateChannelCreateRequest request) {

        log.info("비공개 채널 생성 API 호출 - 참여자 수: {}", request.participantIds().size());

        ChannelDto createdChannel = channelService.createPrivateChannel(request);
        log.info("비공개 채널 생성 완료 - channelId: {}", createdChannel.id());

        return ResponseEntity.status(201).body(createdChannel);
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
        @RequestBody @Valid UpdateChannelRequest request) {

        log.info("채널 수정 API 호출 - channelId: {}", channelId);

        ChannelDto updatedChannel = channelService.updateChannel(channelId, request);
        log.info("채널 수정 완료 - channelId: {}", channelId);

        return ResponseEntity.ok(updatedChannel);
    }

    @Operation(summary = "채널 삭제")
    @DeleteMapping("/{channelId}")
    @ApiResponse(responseCode = "204", description = "채널 삭제 성공")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        log.info("채널 삭제 API 호출 - channelId: {}", channelId);
        channelService.deleteChannel(channelId);
        log.info("채널 삭제 완료 - channelId: {}", channelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "참여 중인 채널 목록 조회", description = "사용자의 ID를 기반으로 자신이 참여 중인 채널 조회")
    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAllByUser(@RequestParam UUID userId) {
        log.info("사용자 참여 채널 조회 API 호출 - userId: {}", userId);
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}
