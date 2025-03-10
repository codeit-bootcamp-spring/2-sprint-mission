package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel getChannel(UUID channelId);

    List<Channel> getAllChannels();

    List<Channel> getUpdatedChannels();

    void registerChannel(String channelName, String userName);

    void updateChannel(UUID channelId, String channelName);

    void deleteChannel(UUID channelId);
}
