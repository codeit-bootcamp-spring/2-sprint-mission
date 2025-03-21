package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDetailDto;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(ChannelDto channelDto);
    Channel createPrivateChannel(ChannelDto channelDto);
    ChannelDetailDto findById(UUID channelId);
    List<ChannelDetailDto> findAllByUserId(UUID userId);
    Channel update(UUID channelId, ChannelUpdateDto channelUpdateDto);
    void delete(UUID channelId);
}
