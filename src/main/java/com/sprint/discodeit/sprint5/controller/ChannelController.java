package com.sprint.discodeit.sprint5.controller;

import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelFindResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelSummaryResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelUpdateRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.discodeit.sprint5.domain.entity.Channel;
import com.sprint.discodeit.sprint5.service.basic.chnnel.ChannelServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Channel API", description = "채널 관련 API입니다.")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelServiceV1 channelService;

    @Operation(summary = "비공개 채널 생성", description = "비공개 채널을 생성합니다.")
    @PostMapping("/private")
    public ResponseEntity<ChannelResponseDto> createPrivateChannel(
            @RequestBody PrivateChannelCreateRequestDto requestDto) {
        return ResponseEntity.ok(channelService.createPrivateChannel(requestDto));
    }

    @Operation(summary = "공개 채널 생성", description = "공개 채널을 생성합니다.")
    @PostMapping("/public")
    public ResponseEntity<ChannelResponseDto> createPublicChannel(
            @RequestBody PublicChannelCreateRequestDto requestDto) {
        return ResponseEntity.ok(channelService.createPublicChannel(requestDto));
    }

    @Operation(summary = "채널 수정", description = "채널 정보를 수정합니다.")
    @PutMapping("/{channelId}")
    public ResponseEntity<Channel> updateChannel(
            @Parameter(description = "수정할 채널의 UUID") @PathVariable String channelId,
            @RequestBody ChannelUpdateRequestDto requestDto) {
        return ResponseEntity.ok(channelService.update(channelId, requestDto));
    }

    @Operation(summary = "채널 삭제", description = "채널을 삭제합니다.")
    @DeleteMapping("/{channelId}")
    public ResponseEntity<String> deleteChannel(
            @Parameter(description = "삭제할 채널의 UUID") @PathVariable String channelId) {
        channelService.delete(UUID.fromString(channelId));
        return ResponseEntity.ok("채널방 삭제되었습니다");
    }

    @Operation(summary = "채널 조회", description = "UUID를 기반으로 채널을 조회합니다.")
    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelFindResponseDto> findChannel(
            @Parameter(description = "조회할 채널의 UUID") @PathVariable String channelId) {
        return ResponseEntity.ok(channelService.findChannelById(UUID.fromString(channelId)));
    }

    @Operation(summary = "내 채널 목록 조회", description = "사용자 UUID를 기반으로 본인이 참여 중인 채널 목록을 조회합니다.")
    @GetMapping("/{usersId}/profile")
    public ResponseEntity<List<ChannelSummaryResponseDto>> getChannelsForusers(
            @Parameter(description = "사용자의 UUID") @PathVariable UUID usersId) {
        return ResponseEntity.ok(channelService.findAllByusersId(usersId));
    }

    @Operation(summary = "공개 채널 목록 조회", description = "모든 공개 채널을 조회합니다.")
    @GetMapping("/public")
    public ResponseEntity<List<ChannelSummaryResponseDto>> getAllPublicChannels() {
        return ResponseEntity.ok(channelService.findAll());
    }
}
