package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channel")
@Tag(name = "Channel", description = "채널 관리 API")
public class ChannelController {
    private final ChannelService channelService;

    @Operation(summary = "공개 채널 생성", description = "새로운 공개 채널을 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "채널 생성 성공",
            content = @Content(schema = @Schema(implementation = Channel.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터",
            content = @Content(examples = @ExampleObject(value = "Invalid request data"))
    )
    @PostMapping("/public")
    public ResponseEntity<Channel> createPublic(
            @Parameter(description = "공개 채널 생성 요청 데이터", required = true)
            @RequestBody PublicChannelCreateRequest request) {
        Channel createdChannel = channelService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @Operation(summary = "비공개 채널 생성", description = "새로운 비공개 채널을 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "채널 생성 성공",
            content = @Content(schema = @Schema(implementation = Channel.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터",
            content = @Content(examples = @ExampleObject(value = "Invalid request data"))
    )
    @PostMapping("/private")
    public ResponseEntity<Channel> createPrivate(
            @Parameter(description = "비공개 채널 생성 요청 데이터", required = true)
            @RequestBody PrivateChannelCreateRequest request) {
        Channel createdChannel = channelService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @Operation(summary = "채널 정보 수정", description = "기존 채널 정보를 수정합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "채널 수정 성공",
            content = @Content(schema = @Schema(implementation = Channel.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "채널을 찾을 수 없음",
            content = @Content(examples = @ExampleObject(value = "Channel with ID {channelId} not found"))
    )
    @PutMapping("/{channelId}")
    public ResponseEntity<Channel> update(
            @Parameter(description = "수정할 채널 Id", required = true)
            @PathVariable("channelId") UUID channelId,

            @Parameter(description = "채널 수정 요청 데이터", required = true)
            @RequestBody PublicChannelUpdateRequest request) {
        Channel udpatedChannel = channelService.update(channelId, request);
        return ResponseEntity.ok(udpatedChannel);
    }

    @Operation(summary = "채널 삭제", description = "특정 채널을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @ApiResponse(
            responseCode = "404",
            description = "채널을 찾을 수 없음",
            content = @Content(examples = @ExampleObject(value = "Channel with ID {channelId} not found"))
    )
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 채널 ID", required = true)
            @PathVariable("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자별 채널 조회", description = "특정 사용자가 참여한 모든 채널을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(type = "array", implementation = ChannelDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = @Content(examples = @ExampleObject(value = "User with ID {userId} not found"))
    )
    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAll(
            @Parameter(description = "조회할 사용자 Id", required = true)
            @RequestParam UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }
}