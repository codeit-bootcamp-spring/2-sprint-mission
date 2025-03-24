package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    ChannelResponse createPublicChannel(PublicChannelCreateRequest request);
    ChannelResponse createPrivateChannel(PrivateChannelCreateRequest request);
    Optional<ChannelResponse> find(UUID channelId);
    List<ChannelResponse> findAllByUserId(UUID userId);
    void update(ChannelUpdateRequest request);
    void delete(UUID channelId);
}
