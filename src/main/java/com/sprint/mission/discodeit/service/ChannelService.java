package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto);

    Channel createPublic(ChannelCreatePublicDto channelCreatePublicDto);

    List<Channel> findAll();

    Channel update(UUID channelId, String newName, String newDescription);

    void delete(UUID channelId);
}