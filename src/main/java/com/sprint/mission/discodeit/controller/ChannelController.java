package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
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

    @PostMapping("/public")
    public ResponseEntity<Channel> createUser(@RequestBody ChannelCreatePublicDto channelCreatePublicDto) {
        Channel channel = channelService.createPublic(channelCreatePublicDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @PostMapping("/private")
    public ResponseEntity<Channel> createUser(@RequestBody ChannelCreatePrivateDto channelCreatePrivateDto) {
        Channel channel = channelService.createPrivate(channelCreatePrivateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<Channel> updateChannel(@PathVariable UUID channelId,
                                                 @RequestBody ChannelUpdateDto channelUpdateDto) {
        Channel channel = channelService.update(channelId, channelUpdateDto);
        return ResponseEntity.ok(channel);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> getChannelsByUserId(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}
