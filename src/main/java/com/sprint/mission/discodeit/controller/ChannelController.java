package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @PostMapping("/public")
    @Override
    public ResponseEntity<ChannelDto> createPublic(
        @Valid @RequestBody PublicChannelCreateRequest publicChannelCreateRequest
    ) {
        ChannelDto channelDto = channelService.createPublicChannel(
            publicChannelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(channelDto);
    }

    @PostMapping("/private")
    @Override
    public ResponseEntity<ChannelDto> createPrivate(
        @Valid @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest
    ) {
        ChannelDto channelDto = channelService.createPrivateChannel(
            privateChannelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(channelDto);
    }

    @PatchMapping("/{channelId}")
    @Override
    public ResponseEntity<ChannelDto> update(
        @PathVariable(name = "channelId") UUID channelId,
        @Valid @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest
    ) {
        ChannelDto channelDto = channelService.updateChannel(channelId, publicChannelUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(channelDto);
    }

    @DeleteMapping("/{channelId}")
    @Override
    public ResponseEntity<Void> delete(
        @PathVariable("channelId") UUID channelId
    ) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("")
    @Override
    public ResponseEntity<List<ChannelDto>> findMyChannel(
        @RequestParam("userId") UUID userId
    ) {
        List<ChannelDto> findMyChannelList = channelService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(findMyChannelList);
    }
}
