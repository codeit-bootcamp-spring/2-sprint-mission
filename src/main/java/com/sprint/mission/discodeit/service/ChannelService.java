package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    ChannelDto createPrivateChannel(PrivateChannelCreateRequest request);

    ChannelDto createPublicChannel(PublicChannelCreateRequest request);

    ChannelDto getChannelById(UUID channelId);

    List<Channel> getChannelsByName(String name);

    List<ChannelDto> findAllByUserId(UUID userId);

    ChannelDto updateChannel(UUID channelId, UpdateChannelRequest request);

    void deleteChannel(UUID channelId);
}
