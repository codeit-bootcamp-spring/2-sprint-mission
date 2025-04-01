package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDTO;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTO;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDTO;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPrivateChannel(CreatePrivateChannelDTO dto);

    Channel createPublicChannel(CreatePublicChannelDTO dto);

    ChannelResponseDTO searchChannel(UUID channelId);

    List<ChannelResponseDTO> searchAllByUserId(UUID userId);

    Channel updateChannel(UUID channelId, UpdateChannelDTO dto);

    void deleteChannel(UUID channelId);
}
