package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelDTO;
import com.sprint.mission.discodeit.dto.CreatePublicChannelDTO;
import com.sprint.mission.discodeit.dto.UpdateChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponseDTO createPublic(CreatePublicChannelDTO createPublicChannelDTO);
    ChannelResponseDTO createPrivate(CreatePrivateChannelDTO createPrivateChannelDTO);
    ChannelResponseDTO find(UUID channelId);
    List<ChannelResponseDTO> findAll();
    ChannelResponseDTO update(UpdateChannelDTO updateChannelDTO);
    void delete(UUID channelId);
}
