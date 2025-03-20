package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.dto.channel.ChannelByIdResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelType type, String name, String description, List<UUID> privateUserIds);

    ChannelByIdResponse find(UUID channelId);

    List<ChannelByIdResponse> findAllByUserId(UUID userId);

    Channel update(ChannelUpdateRequest updateRequest);

    void delete(UUID channelId);
}