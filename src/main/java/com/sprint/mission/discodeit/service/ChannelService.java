package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelDto createPublicChannel(CreatePublicChannelRequest request);

    ChannelDto createPrivateChannel(CreatePrivateChannelRequest request);

    ChannelDto findByChannelId(UUID channelId);

    List<ChannelDto> findAllByUserId(UUID userId);

    ChannelDto update(UUID channelId, UpdateChannelRequest request);

    void remove(UUID channelId);
}
