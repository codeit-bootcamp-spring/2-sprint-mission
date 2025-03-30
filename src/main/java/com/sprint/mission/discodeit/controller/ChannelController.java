package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
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

    public ChannelRequest createPublicChannel(ChannelRegisterRequest channelRegisterRequest) {
        return channelService.createPublic(channelRegisterRequest);
    }

    public ChannelRequest createPrivateChannel(ChannelRegisterRequest channelRegisterRequest, List<UUID> memberIds) {
        return channelService.createPrivate(channelRegisterRequest, memberIds);
    }

    public List<ChannelRequest> findAllByUserId(UUID userId) {
        return channelService.findAllByUserId(userId);
    }

    public ChannelRequest updatePublicChannelName(UUID channelId, String channelName) {
        return channelService.updatePublicChannelName(channelId, channelName);
    }

    public ChannelRequest addPrivateChannelMember(UUID channelId, String friendEmail) {
        UserResult friend = userService.findByEmail(friendEmail);

        return channelService.addPrivateChannelMember(channelId, friend.id());
    }

    public ChannelRequest findById(UUID channelId) {
        return channelService.findById(channelId);
    }
}
