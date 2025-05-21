package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

    private final ChannelService channelService;

    @Operation(summary = "Public Channel 생성")
    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublic(
            @RequestBody @Valid PublicChannelCreateRequest publicChannelCreateRequest) {
        log.info("Received public channel create request: {}", publicChannelCreateRequest);
        ChannelDto channelDto = channelService.createPublic(publicChannelCreateRequest);
        log.info("Public channel created successfully: channelId={}", channelDto.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
    }

    @Operation(summary = "Private Channel 생성")
    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createPrivate(
            @RequestBody @Valid PrivateChannelCreateRequest privateChannelCreateRequest) {
        log.info("Received private channel create request: {}", privateChannelCreateRequest);
        ChannelDto channelDto = channelService.createPrivate(privateChannelCreateRequest);
        log.info("Private channel created successfully: channelId={}", channelDto.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
    }

    @Operation(summary = "Channel 정보 수정")
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable UUID channelId,
                                                    @RequestBody @Valid ChannelUpdateRequest channelUpdateRequest) {
        log.info("Received channel update request: channelId={}, updateDto={}", channelId, channelUpdateRequest);
        ChannelDto channelDto = channelService.update(channelId, channelUpdateRequest);
        log.info("Channel updated successfully: channelId={}", channelDto.id());

        return ResponseEntity.ok(channelDto);
    }

    @Operation(summary = "Channel 삭제")
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        log.info("Received channel delete request: channelId={}", channelId);
        channelService.delete(channelId);
        log.info("Channel deleted successfully: channelId={}", channelId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "User가 참여 중인 Channel 목록 조회")
    @GetMapping
    public ResponseEntity<List<ChannelDto>> getChannelsByUserId(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}
