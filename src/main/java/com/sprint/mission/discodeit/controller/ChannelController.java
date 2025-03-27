package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final UserService userService;

    public ChannelDto createPublicChannel(ChannelRegisterDto channelRegisterDto) {
        return channelService.createPublic(channelRegisterDto);
    }

    public ChannelDto createPrivateChannel(ChannelRegisterDto channelRegisterDto, List<UUID> memberIds) {
        return channelService.createPrivate(channelRegisterDto, memberIds);
    }

    public List<ChannelDto> findAllByUserId(UUID userId) {
        return channelService.findAllByUserId(userId);
    }

    public ChannelDto updatePublicChannelName(UUID channelId, String channelName) {
        return channelService.updatePublicChannelName(channelId, channelName);
    }

    public ChannelDto addPrivateChannelMember(UUID channelId, String friendEmail) {
        UserDto friend = userService.findByEmail(friendEmail);

        return channelService.addPrivateChannelMember(channelId, friend.id());
    }

    public ChannelDto findById(UUID channelId) {
        return channelService.findById(channelId);
    }
}
