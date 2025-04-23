package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto);

    ChannelDto createPublic(ChannelCreatePublicDto channelCreatePublicDto);

    ChannelDto findById(UUID channelId);

    List<ChannelDto> findAllByUserId(UUID userId);

    ChannelDto update(UUID channelId, ChannelUpdateDto channelUpdateDto);

    void delete(UUID channelId);
}