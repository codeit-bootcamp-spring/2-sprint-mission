package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelRequest createPublic(ChannelCreateRequest channelRegisterRequest);

    ChannelRequest createPrivate(ChannelCreateRequest channelRegisterRequest, List<UUID> channelMemberIds);

    ChannelRequest addPrivateChannelMember(UUID channelId, UUID friendId);

    ChannelRequest getById(UUID id);

    List<ChannelRequest> getAllByUserId(UUID userId);

    ChannelRequest updatePublicChannelName(UUID id, String name);

    void delete(UUID channelId);
}
