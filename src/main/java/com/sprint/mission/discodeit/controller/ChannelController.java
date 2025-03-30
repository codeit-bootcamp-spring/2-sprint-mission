package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.ChannelCreateResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.ChannelCreateDto;
import com.sprint.mission.discodeit.service.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.service.dto.ChannelUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ChannelCreateResponseDto> createPublic(@RequestBody ChannelCreateDto createDto) {
        Channel channel = channelService.createPublicChannel(createDto);
        ChannelCreateResponseDto response = ChannelCreateResponseDto.convertToResponseDto(channel);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelCreateResponseDto> createPrivate(@RequestBody ChannelCreateDto createDto) {
        Channel channel = channelService.createPrivateChannel(createDto);
        ChannelCreateResponseDto response = ChannelCreateResponseDto.convertToResponseDto(channel);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChannelCreateResponseDto> update(@RequestBody ChannelUpdateDto updateDto) {     //@PathVariable UUID id
        Channel channel = channelService.updateChannel(updateDto);
        ChannelCreateResponseDto response = ChannelCreateResponseDto.convertToResponseDto(channel);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        ChannelResponseDto channelResponseDto = channelService.findChannelById(id);
        channelService.deleteChannel(id, channelResponseDto.userId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ChannelResponseDto>> getAllByUserId(@PathVariable UUID userId) {
        List<ChannelResponseDto> channelList = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channelList);
    }

}
