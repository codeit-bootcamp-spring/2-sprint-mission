package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto);

    Channel createPublic(ChannelCreatePublicDto channelCreatePublicDto);

    ChannelDto findById(UUID channelId);

    List<ChannelDto> findAllByUserId(UUID userId);

    Channel update(ChannelUpdateDto channelUpdateDto);

    void delete(UUID channelId);
}