package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResult createPublic(PublicChannelCreateRequest channelRegisterRequest);

    ChannelResult createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest);

    ChannelResult addPrivateMember(UUID channelId, UUID friendId);

    ChannelResult getById(UUID id);

    List<ChannelResult> getAllByUserId(UUID userId);

    ChannelResult updatePublic(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest);

    void delete(UUID channelId);
}
