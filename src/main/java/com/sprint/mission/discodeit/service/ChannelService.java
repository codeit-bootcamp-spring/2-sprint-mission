package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;


public interface ChannelService {
    Channel create(String channelName, String description);
    Channel getChannel(UUID channelId);
    List<Channel> getAllChannel();
    Channel update(UUID channelId, String changeChannel, String changeDescription);
    void delete(UUID channelId);
}
