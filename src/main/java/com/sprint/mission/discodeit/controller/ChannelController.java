package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelInfoDto;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createPrivateChannel(@RequestBody CreatePrivateChannelRequest channelDto) {
        Channel channel = channelService.createPrivateChannel(channelDto);

        return ResponseEntity.ok(channel.getMembers());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createPublicChannel(@RequestBody CreatePublicChannelRequest channelDto) {
        Channel channel = channelService.createPublicChannel(channelDto);

        return ResponseEntity.ok("Public 채널이 생성되었습니다.");
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateChannel(@PathVariable UUID channelId, @RequestBody UpdateChannelRequest channelDto) {
        channelService.updateChannel(channelId, channelDto);

        return ResponseEntity.ok("채널 수정이 완료되었습니다.");
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);

        return ResponseEntity.ok("채널이 삭제되었습니다.");
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAllChannels(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = UUID.fromString(token);

        List<ChannelInfoDto> channels = channelService.findAllByUserId(userId);

        return ResponseEntity.ok(channels);
    }
}
