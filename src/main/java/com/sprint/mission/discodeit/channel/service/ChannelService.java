package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.dto.service.ChannelResult;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResult createPublic(PublicChannelCreateRequest channelRegisterRequest);

    ChannelResult createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest);

    ChannelResult getById(UUID id);

    List<ChannelResult> getAllByUserId(UUID userId);

    ChannelResult updatePublic(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest);

    void delete(UUID channelId);
}
