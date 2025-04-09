package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Tag(name = "Channel")
public class ChannelController {

    private final ChannelService channelService;

    @Operation(summary = "Public Channel 생성", operationId = "create_3")
    @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨")
    @PostMapping("/public")
    public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @Operation(summary = "Private Channel 생성", operationId = "create_4")
    @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨")
    @PostMapping("/private")
    public ResponseEntity<Channel> createPrivate(@RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @Operation(summary = "Channel 정보 수정", description = "update_3")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음"),
            @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨")
    })
    @PatchMapping("/{channelId}")
    public ResponseEntity<Channel> updateChannel(
            @Parameter(
                    name = "channelId",
                    description = "수정할 Channel ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID channelId,
            @RequestBody PublicChannelUpdateRequest request) {
        Channel updatedChannel = channelService.updateChannel(channelId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedChannel);
    }

    @Operation(summary = "Channel 삭제", operationId = "delete_2")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음"),
            @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨")
    })
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannelById(
            @Parameter(
                    name = "channelId",
                    description = "삭제할 Channel ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "User가 참여 중인 Channel목록 조회", operationId = "findAll_1")
    @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAllChannelsByUserId(
            @Parameter(
                    name = "userId",
                    description = "조회할 User ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )

            @RequestParam UUID userId) {

        List<ChannelDto> channels = channelService.findAllChannelsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
}
