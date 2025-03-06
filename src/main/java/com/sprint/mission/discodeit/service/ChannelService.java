package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;


public interface ChannelService {
    Channel create(Channel channel);
    Channel getChannel(String channelName);
    List<Channel> getAllChannel();
    Channel update(String channel, String changeChannel, String changeDescription);
    void delete(String channelName);
}
