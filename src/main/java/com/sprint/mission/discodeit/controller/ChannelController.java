package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDTO;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTO;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDTO;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public Channel createPublicChannel(@RequestBody CreatePublicChannelDTO dto) {
        return channelService.createPublicChannel(dto);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public Channel createPrivateChannel(@RequestBody CreatePrivateChannelDTO dto) {
        return channelService.createPrivateChannel(dto);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
    public Channel updateChannel(@PathVariable UUID channelId, @RequestBody UpdateChannelDTO dto) {
        return channelService.updateChannel(channelId, dto);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public void deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<ChannelResponseDTO> getAllChannels(@PathVariable UUID userId) {
        return channelService.searchAllByUserId(userId);
    }
}
