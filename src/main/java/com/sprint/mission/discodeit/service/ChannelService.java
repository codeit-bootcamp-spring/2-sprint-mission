package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(Channel channel);
    Channel getChannel(String channelName);
    List<Channel> getChannels();

    void updateChannel(UUID id, String channelName);
    void deleteChannel(UUID id);
}
