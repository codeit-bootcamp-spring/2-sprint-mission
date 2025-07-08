package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.CustomUserDetailsService.CustomUserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublic(
        @Valid @RequestBody PublicChannelCreateRequest request) {
        ChannelDto createdChannel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createPrivate(
        @Valid @RequestBody PrivateChannelCreateRequest request) {
        ChannelDto createdChannel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @GetMapping("/public")
    public ResponseEntity<List<ChannelDto>> findAllPublic() {
        List<ChannelDto> publicChannels = channelService.findAllPublic();
        return ResponseEntity.status(HttpStatus.OK).body(publicChannels);
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAll(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        UUID userId = principal.user().getId();

        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelDto> findById(@PathVariable("channelId") UUID channelId) {
        ChannelDto channel = channelService.find(channelId);
        return ResponseEntity.status(HttpStatus.OK).body(channel);
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> update(@PathVariable("channelId") UUID channelId,
        @Valid @RequestBody PublicChannelUpdateRequest request) {
        ChannelDto udpatedChannelDto = channelService.update(channelId, request);
        return ResponseEntity.status(HttpStatus.OK).body(udpatedChannelDto);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
