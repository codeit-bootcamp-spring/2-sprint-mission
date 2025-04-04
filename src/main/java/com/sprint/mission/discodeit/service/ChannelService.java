package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelCreateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResult createPublic(PublicChannelCreateRequest channelRegisterRequest);

    ChannelResult createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest);

    ChannelResult addPrivateChannelMember(UUID channelId, UUID friendId);

    ChannelResult getById(UUID id);

    List<ChannelResult> getAllByUserId(UUID userId);

    ChannelResult updatePublicChannelName(UUID id, String name);

    void delete(UUID channelId);
}
