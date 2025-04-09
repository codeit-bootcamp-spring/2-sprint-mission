package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(PrivateChannelCreateRequest request);

    Channel create(PublicChannelCreateRequest request);

    ChannelDto searchChannel(UUID channelId);

    List<ChannelDto> findAllChannelsByUserId(UUID userId);

    Channel updateChannel(UUID channelId, PublicChannelUpdateRequest dto);

    void deleteChannel(UUID channelId);
}
