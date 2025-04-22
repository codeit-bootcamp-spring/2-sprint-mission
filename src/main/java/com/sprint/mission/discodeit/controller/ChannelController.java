package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResult;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Channel", description = "채널 관련 API")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final UserService userService;

    @Operation(
            summary = "공개 채널 생성",
            description = "이름과 내용을 포함한 공개채널 생성"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공개 채널 생성 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PostMapping("/public")
    public ResponseEntity<ChannelResult> createPublic(
            @Parameter(description = "공개 채널 정보", required = true)
            @Valid @RequestBody PublicChannelCreateRequest channelRegisterRequest) {

        return ResponseEntity.ok(channelService.createPublic(channelRegisterRequest));
    }

    @Operation(
            summary = "비공개 채널 생성",
            description = "참가자만 포함한 비공개 채널 생성"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비공개 채널 생성 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PostMapping("/private")
    public ResponseEntity<ChannelResult> createPrivate(
            @Parameter(description = "비공개 채널 정보", required = true)
            @Valid @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {

        return ResponseEntity.ok(channelService.createPrivate(privateChannelCreateRequest));
    }

    @Operation(
            summary = "채널 ID로 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelResult> getById(
            @Parameter(description = "채널 ID", required = true)
            @PathVariable UUID channelId) {

        return ResponseEntity.ok(channelService.getById(channelId));
    }

    @Operation(
            summary = "유저 ID로 조회",
            description = "유저가 속한 공개, 비공개 채널 반환"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @GetMapping
    public ResponseEntity<List<ChannelResult>> getAllByUserId(
            @Parameter(description = "유저 ID", required = true)
            @RequestParam(value = "userId", required = false) UUID userId) {

        return ResponseEntity.ok(channelService.getAllByUserId(userId));
    }

    @Operation(
            summary = "공개 채널 수정"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공개 채널 수정 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelResult> updatePublic(
            @Parameter(description = "채널 ID", required = true)
            @PathVariable UUID channelId,

            @Parameter(description = "채널 수정 정보", required = true)
            @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {

        ChannelResult channelResult = channelService.updatePublic(channelId, publicChannelUpdateRequest);

        return ResponseEntity.ok(channelResult);
    }

    @Operation(
            summary = "비공개 채널 멤버 추가"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비공개 채널 멤버 추가 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PostMapping("/private/{channelId}/members")
    public ResponseEntity<ChannelResult> addPrivateChannelMember(
            @Parameter(description = "채널 ID", required = true)
            @PathVariable UUID channelId,

            @Parameter(description = "친구 이메일", required = true)
            @RequestBody String friendEmail) {

        UserResult friend = userService.getByEmail(friendEmail);
        ChannelResult channelResult = channelService.addPrivateMember(channelId, friend.id());

        return ResponseEntity.ok(channelResult);
    }

    @Operation(
            summary = "채널 삭제"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "채널 ID", required = true)
            @PathVariable UUID channelId) {

        channelService.delete(channelId);

        return ResponseEntity.noContent().build();
    }
}
