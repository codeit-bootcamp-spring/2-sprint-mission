package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

    private final ChannelService channelService;

    @Operation(summary = "Public Channel 생성")
    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createUser(@RequestBody ChannelCreatePublicDto channelCreatePublicDto) {
        ChannelDto channelDto = channelService.createPublic(channelCreatePublicDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
    }

    @Operation(summary = "Private Channel 생성")
    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createUser(@RequestBody ChannelCreatePrivateDto channelCreatePrivateDto) {
        ChannelDto channelDto = channelService.createPrivate(channelCreatePrivateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
    }

    @Operation(summary = "Channel 정보 수정")
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable UUID channelId,
                                                    @RequestBody ChannelUpdateDto channelUpdateDto) {
        ChannelDto channelDto = channelService.update(channelId, channelUpdateDto);
        return ResponseEntity.ok(channelDto);
    }

    @Operation(summary = "Channel 삭제")
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "User가 참여 중인 Channel 목록 조회")
    @GetMapping
    public ResponseEntity<List<ChannelDto>> getChannelsByUserId(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}
