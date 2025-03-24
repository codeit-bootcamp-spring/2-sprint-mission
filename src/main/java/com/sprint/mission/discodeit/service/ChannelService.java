package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivate;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublic;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdate;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublic(ChannelCreatePublic dto);
    Channel createPrivate(ChannelCreatePrivate dto);
    ChannelResponse find(UUID channelId);
    List<ChannelResponse> findAllByUserID(UUID userID);
    Channel update(ChannelUpdate dto);
    void delete(UUID channelId);
}
