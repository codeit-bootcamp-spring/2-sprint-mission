package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelRequest createPublic(ChannelRegisterRequest channelRegisterRequest);

    ChannelRequest createPrivate(ChannelRegisterRequest channelRegisterRequest, List<UUID> channelMemberIds);

    ChannelRequest addPrivateChannelMember(UUID channelId, UUID friendId);

    ChannelRequest findById(UUID id);

    List<ChannelRequest> findAllByUserId(UUID userId);

    ChannelRequest updatePublicChannelName(UUID id, String name);

    void delete(UUID channelId);
}
