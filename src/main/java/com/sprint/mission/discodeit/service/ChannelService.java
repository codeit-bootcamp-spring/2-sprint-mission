package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {
    Channel create(Channel channel);
    Channel findByChannelName(String channelName);
    List<Channel> findAll();
    Channel update(String channelName, Channel channel);
    void delete(String channelName);
}
