package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "Channel API")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/public")
    @Operation(summary = "Public Channel 생성")
    @ApiResponse(responseCode = "201", description = "Public Channel 이 성공적으로 생성됨")
    public ResponseEntity<ChannelResponseDto> createPublicChannel(
            @RequestBody ChannelCreatePublicDto channelCreateRequest
    ) {
        ChannelResponseDto publicChannel = channelService.createPublic(channelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(publicChannel);
    }


    @PostMapping("/private")
    @Operation(summary = "Private Channel 생성")
    @ApiResponse(responseCode = "200", description = "Private Channel 이 성공적으로 생성됨")
    @ApiResponse(responseCode = "400", description = "Private Channel 이 생성되지 않음", content = @Content(examples = @ExampleObject(value = "User does not exist")))
    public ResponseEntity<ChannelResponseDto> createPrivateChannel(
            @RequestBody ChannelCreatePrivateDto channelCreateRequest
    ) {
        ChannelResponseDto privateChannel = channelService.createPrivate(channelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(privateChannel);
    }


    @PatchMapping("/{channelId}")
    @Operation(summary = "Channel 정보 수정")
    @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "Channel does not found")))
    @ApiResponse(responseCode = "400", description = "Private Channel은 수정 할 수 없음", content = @Content(examples = @ExampleObject(value = "Private channels cannot be changed")))
    @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨")
    public ResponseEntity<ChannelResponseDto> updateChannel(
            @PathVariable @Parameter(description = "수정 할 Channel ID") UUID channelId,
            @RequestBody ChannelUpdateDto channelUpdateRequest
    ) {

        ChannelResponseDto updateChannel = channelService.update(channelId, channelUpdateRequest);
        return ResponseEntity.ok(updateChannel);
    }


    @DeleteMapping("/{channelId}")
    @Operation(summary = "Channel 삭제")
    @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "Channel does not found")))
    @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨")
    public ResponseEntity<Channel> deleteChannel(
            @PathVariable @Parameter(description = "삭제 할 Channel ID") UUID channelId
    ) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    @Operation(summary = "User가 참여 중인 Channel 목록 조회")
    @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공")
    public ResponseEntity<List<ChannelResponseDto>> findChannelByUserId(
            @RequestParam @Parameter(description = "조회 할 User ID") UUID userId
    ) {
        List<ChannelResponseDto> channelFindAllByUserIdResponse = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channelFindAllByUserIdResponse);
    }
}
