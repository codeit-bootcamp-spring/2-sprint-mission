package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.ChannelDetailsDto;
import com.sprint.mission.discodeit.DTO.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.DTO.CreatePublicChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
        ChannelDetailsDto createPublicChannel(CreatePublicChannelDto dto);
        ChannelDetailsDto createPrivateChannel(CreatePrivateChannelDto dto);
        ChannelDetailsDto find(UUID channelId);
        List<ChannelDetailsDto> findAllByUserId(UUID userId);
        void update(UUID channelId, String newName, String newDescription);
        void delete(UUID channelId);
        boolean exists(UUID channelId);
}
