package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelCreationRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResult createPublic(ChannelCreateRequest channelRegisterRequest);

    ChannelResult createPrivate(PrivateChannelCreationRequest privateChannelCreationRequest);

    ChannelResult addPrivateChannelMember(UUID channelId, UUID friendId);

    ChannelResult getById(UUID id);

    List<ChannelResult> getAllByUserId(UUID userId);

    ChannelResult updatePublicChannelName(UUID id, String name);

    void delete(UUID channelId);
}
