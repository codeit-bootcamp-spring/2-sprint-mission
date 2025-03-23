package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublic(PublicChanRequest publicChanRequest);
    Channel createPrivate(PrivateChanRequest privateChanRequest);
    Channel find(FindChannelRequest findChannelRequest);
    List<Channel> findAllByUserId(FindAllChannelRequest findAllChannelRequest);
    Channel update(UpdateChannelRequest updateChannelRequest);
    void delete(UUID channelId);
}
