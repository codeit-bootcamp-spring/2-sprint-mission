package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.CreateChannelParam;
import com.sprint.mission.discodeit.dto.service.channel.FindChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelParam;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDTO createPublicChannel(CreateChannelParam createChannelParam);
    ChannelDTO createPrivateChannel(List<UUID> userIds, CreateChannelParam createChannelParam);
    FindChannelDTO find(UUID channelId);
    List<FindChannelDTO> findAllByUserId(UUID userId);
    UUID update(UpdateChannelParam updateChannelParam);
    void delete(UUID channelId);
}
