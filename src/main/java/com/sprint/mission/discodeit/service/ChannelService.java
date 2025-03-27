package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.dto.channel.ChannelByIdResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(PrivateChannelRequest privateRequest);

    Channel create(PublicChannelRequest publicRequest);

    ChannelByIdResponse find(UUID channelId);

    List<ChannelByIdResponse> findAllByUserId(UUID userId);

    Channel update(UUID id, ChannelUpdateRequest updateRequest);

    void delete(UUID channelId);
}