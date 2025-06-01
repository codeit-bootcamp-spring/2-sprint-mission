package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest);

    ChannelDto createPublic(PublicChannelCreateRequest publicChannelCreateRequest);

    ChannelDto findById(UUID channelId);

    List<ChannelDto> findAllByUserId(UUID userId);

    ChannelDto update(UUID channelId, ChannelUpdateRequest channelUpdateRequest);

    void delete(UUID channelId);
}